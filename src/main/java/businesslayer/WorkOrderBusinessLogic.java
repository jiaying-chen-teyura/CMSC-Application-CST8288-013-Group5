package businesslayer;

import java.util.List;
import businesslayer.adapter.ExternalJobRequestAdapter;
import businesslayer.adapter.ExternalJobRequestForm;
import businesslayer.builder.WorkOrderBuilder;
import businesslayer.strategy.CreditContext;
import businesslayer.strategy.WorkOrderCreditStrategy;
import dataaccesslayer.LedgerDao;
import dataaccesslayer.LedgerDaoImpl;
import dataaccesslayer.WorkOrderDao;
import dataaccesslayer.WorkOrderDaoImpl;
import transferobjects.AccountTransactionDTO;
import transferobjects.WorkOrderDTO;

/**
 * Backs the Work Order use cases (Submit / Accept / Complete), covering
 * both members ("work orders... on behalf of members who do not want to
 * do the work themselves") and external clients (via the Adapter pattern).
 * 
 * @author Le Bao Thach Nguyen 
 */
public class WorkOrderBusinessLogic {

    private final WorkOrderDao workOrderDao;
    private final LedgerDao ledgerDao;

    public WorkOrderBusinessLogic() {
        this(new WorkOrderDaoImpl(), new LedgerDaoImpl());
    }

    public WorkOrderBusinessLogic(WorkOrderDao workOrderDao, LedgerDao ledgerDao) {
        this.workOrderDao = workOrderDao;
        this.ledgerDao = ledgerDao;
    }

    /** Submitted by a logged-in member through the web UI. */
    public WorkOrderDTO submitMemberWorkOrder(int memberUserId, String description, WorkOrderDTO.Priority priority,
                                               double estEquipmentCost, double estMaterialCost, double estLabourCost)
            throws ValidationException {
        WorkOrderDTO wo;
        try {
            wo = new WorkOrderBuilder()
                    .forMember(memberUserId)
                    .description(description)
                    .priority(priority)
                    .estimatedEquipmentCost(estEquipmentCost)
                    .estimatedMaterialCost(estMaterialCost)
                    .estimatedLabourCost(estLabourCost)
                    .build();
        } catch (IllegalStateException e) {
            throw new ValidationException(e.getMessage());
        }
        int id = workOrderDao.submitWorkOrder(wo);
        wo.setWorkOrderId(id);
        return wo;
    }

    /**
     * Submitted through the external "request a job" form. Uses the Adapter
     * pattern (ExternalJobRequestAdapter) to translate that form's shape
     * into our WorkOrderDTO/Builder pipeline.
     */
    public WorkOrderDTO submitExternalWorkOrder(ExternalJobRequestForm form) {
        WorkOrderDTO wo = new ExternalJobRequestAdapter().adapt(form);
        int id = workOrderDao.submitWorkOrder(wo);
        wo.setWorkOrderId(id);
        return wo;
    }

    public void acceptWorkOrder(int workOrderId, int shopTechId) throws ValidationException {
        WorkOrderDTO wo = workOrderDao.getWorkOrderById(workOrderId);
        if (wo == null) throw new ValidationException("Work order not found.");
        if (wo.getStatus() != WorkOrderDTO.Status.SUBMITTED && wo.getStatus() != WorkOrderDTO.Status.QUOTED) {
            throw new ValidationException("That work order is not available to accept.");
        }
        workOrderDao.acceptWorkOrder(workOrderId, shopTechId);
    }

    /**
     * A Shop-Tech "checks in" to a work order they've already accepted - mirrors
     * MaintenanceBusinessLogic.startMaintenance. This is the scheduling/arrangement step that was
     * previously missing: accepting a work order no longer immediately starts the clock: the
     * Shop-Tech accepts it (ACCEPTED), lines up when they'll actually do the job, then explicitly
     * starts it (IN_PROGRESS) when they begin work, and completes it when done.
     */
    public void startWorkOrder(int workOrderId, int shopTechId) throws ValidationException {
        WorkOrderDTO wo = workOrderDao.getWorkOrderById(workOrderId);
        if (wo == null) throw new ValidationException("Work order not found.");
        if (wo.getAssignedShopTechId() == null || wo.getAssignedShopTechId() != shopTechId) {
            throw new ValidationException("This work order is assigned to a different Shop-Tech.");
        }
        if (wo.getStatus() != WorkOrderDTO.Status.ACCEPTED) {
            throw new ValidationException("Only an accepted work order can be started.");
        }
        workOrderDao.startWorkOrder(workOrderId);
    }

    public void completeWorkOrder(int workOrderId, int shopTechId) throws ValidationException {
        WorkOrderDTO wo = workOrderDao.getWorkOrderById(workOrderId);
        if (wo == null) throw new ValidationException("Work order not found.");
        if (wo.getAssignedShopTechId() == null || wo.getAssignedShopTechId() != shopTechId) {
            throw new ValidationException("Only the assigned Shop-Tech can complete this work order.");
        }
        if (wo.getStatus() != WorkOrderDTO.Status.ACCEPTED && wo.getStatus() != WorkOrderDTO.Status.IN_PROGRESS) {
            throw new ValidationException("Only an accepted or in-progress work order can be completed.");
        }

        double labourCost = wo.getQuotedPrice() != null ? wo.getQuotedPrice() : wo.getEstimatedLabourCost();

        // Guard rail: account_transactions.amount has a DB-level CHECK (amount > 0). A $0 labour
        // cost (e.g. an external job request left at its default rate) would otherwise reach
        // recordTransaction and blow up as an uncaught RuntimeException. Fail cleanly instead.
        if (labourCost <= 0) {
            throw new ValidationException(
                    "This work order has no labour cost or quote set (it's currently $0), so it can't be "
                    + "completed yet. Set a quoted price greater than $0 before completing it.");
        }
        // Strategy pattern (required pattern): RUSH vs STANDARD credit math is swappable.
        double credit = new CreditContext(new WorkOrderCreditStrategy(wo.getPriority() == WorkOrderDTO.Priority.RUSH))
                .computeCredit(labourCost);

        workOrderDao.completeWorkOrder(workOrderId, credit);

        AccountTransactionDTO creditTx = new AccountTransactionDTO();
        creditTx.setUserId(shopTechId);
        creditTx.setTransactionType(AccountTransactionDTO.TransactionType.CREDIT);
        creditTx.setActivityType(AccountTransactionDTO.ActivityType.WORK_ORDER);
        creditTx.setAmount(credit);
        creditTx.setDescription("Completed work order #" + workOrderId);
        ledgerDao.recordTransaction(creditTx);

        if (wo.getMemberUserId() != null) {
            AccountTransactionDTO debitTx = new AccountTransactionDTO();
            debitTx.setUserId(wo.getMemberUserId());
            debitTx.setTransactionType(AccountTransactionDTO.TransactionType.DEBIT);
            debitTx.setActivityType(AccountTransactionDTO.ActivityType.WORK_ORDER);
            debitTx.setAmount(labourCost);
            debitTx.setDescription("Work order #" + workOrderId + " fulfilled on your behalf");
            ledgerDao.recordTransaction(debitTx);
        }
    }

    public List<WorkOrderDTO> getOpenWorkOrders() {
        return workOrderDao.getOpenWorkOrders();
    }

    public List<WorkOrderDTO> getWorkOrdersForShopTech(int shopTechId) {
        return workOrderDao.getWorkOrdersForShopTech(shopTechId);
    }

    public List<WorkOrderDTO> getWorkOrdersForMember(int memberUserId) {
        return workOrderDao.getWorkOrdersForMember(memberUserId);
    }
}
