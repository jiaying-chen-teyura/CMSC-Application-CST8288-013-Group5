package businesslayer;

import java.time.LocalDateTime;
import java.util.List;
import businesslayer.adapter.DiagnosticsReading;
import businesslayer.adapter.EquipmentDiagnosticsAdapter;
import businesslayer.adapter.ThirdPartyDiagnosticsPacket;
import businesslayer.observer.MaintenanceAlertEvent;
import businesslayer.observer.MaintenanceAlertService;
import businesslayer.strategy.CreditContext;
import businesslayer.strategy.MaintenanceCreditStrategy;
import dataaccesslayer.*;
import transferobjects.*;

/** Backs FR-05 (Alerts for Predictive Maintenance) and the Shop-Tech maintenance use cases. */
public class MaintenanceBusinessLogic {

    private final MaintenanceDao maintenanceDao;
    private final EquipmentDao equipmentDao;
    private final LedgerDao ledgerDao;

    public MaintenanceBusinessLogic() {
        this(new MaintenanceDaoImpl(), new EquipmentDaoImpl(), new LedgerDaoImpl());
    }

    public MaintenanceBusinessLogic(MaintenanceDao maintenanceDao, EquipmentDao equipmentDao, LedgerDao ledgerDao) {
        this.maintenanceDao = maintenanceDao;
        this.equipmentDao = equipmentDao;
        this.ledgerDao = ledgerDao;
    }

    public List<EquipmentComponentDTO> getComponentsForEquipment(String assetTag) {
        return maintenanceDao.getComponentsForEquipment(assetTag);
    }

    public List<MaintenanceTaskDTO> getOpenAlerts() {
        return maintenanceDao.getOpenMaintenanceTasks();
    }

    public List<MaintenanceTaskDTO> getTasksForShopTech(int shopTechId) {
        return maintenanceDao.getTasksForShopTech(shopTechId);
    }

    /**
     * Adapter Pattern (required pattern) in action: equipment (or its vendor
     * telemetry board) reports diagnostics in its own wire format; we adapt
     * it to our DiagnosticsReading shape before applying business rules,
     * matching FR-03's "equipment reports its real-time status" and FR-05's
     * "equipment diagnostics" monitoring.
     */
    public void ingestDiagnostics(ThirdPartyDiagnosticsPacket packet) {
        DiagnosticsReading reading = new EquipmentDiagnosticsAdapter(packet).toDiagnosticsReading();

        if (reading.isFaultDetected()) {
            equipmentDao.updateStatus(reading.getAssetTag(), EquipmentDTO.Status.MAINTENANCE);
        }
        List<EquipmentComponentDTO> components = maintenanceDao.getComponentsForEquipment(reading.getAssetTag());
        for (EquipmentComponentDTO c : components) {
            if (!c.getComponentName().equalsIgnoreCase(reading.getComponentName())) continue;
            maintenanceDao.addWearHours(c.getComponentId(), reading.getAdditionalUsageHours());
            double newHours = c.getUsageHours() + reading.getAdditionalUsageHours();
            if (reading.isFaultDetected() || newHours >= c.getMaintenanceThresholdHours()) {
                maintenanceDao.setComponentStatus(c.getComponentId(), EquipmentComponentDTO.ComponentStatus.MAINTENANCE_REQUIRED);
                EquipmentDTO equipment = equipmentDao.getEquipmentByAssetTag(reading.getAssetTag());
                MaintenanceAlertService.getInstance().notifyAlert(new MaintenanceAlertEvent(
                        reading.getAssetTag(), equipment != null ? equipment.getEquipmentName() : reading.getAssetTag(),
                        c.getComponentId(), c.getComponentName(), newHours, c.getMaintenanceThresholdHours()));
            }
        }
    }

    public MaintenanceTaskDTO scheduleMaintenance(String assetTag, Integer componentId, int shopTechId,
                                                    MaintenanceTaskDTO.MaintenanceType type, String description,
                                                    MaintenanceTaskDTO.Priority priority, LocalDateTime scheduledStart)
            throws ValidationException {
        if (assetTag == null || assetTag.isBlank()) throw new ValidationException("Asset tag is required.");
        if (description == null || description.isBlank()) throw new ValidationException("Description is required.");
        if (scheduledStart == null) throw new ValidationException("Scheduled start time is required.");
        TimeSlotValidation.validateQuarterHourSlot(scheduledStart, "Scheduled start");

        MaintenanceTaskDTO task = new MaintenanceTaskDTO();
        task.setAssetTag(assetTag);
        task.setComponentId(componentId);
        task.setAssignedShopTechId(shopTechId);
        task.setMaintenanceType(type);
        task.setDescription(description);
        task.setPriority(priority);
        task.setScheduledStart(scheduledStart);
        task.setStatus(MaintenanceTaskDTO.Status.SCHEDULED);
        int id = maintenanceDao.createMaintenanceTask(task);
        task.setMaintenanceId(id);
        equipmentDao.updateStatus(assetTag, EquipmentDTO.Status.MAINTENANCE);
        return task;
    }

    public void performMaintenance(int maintenanceId, int shopTechId, double hoursSpent) throws ValidationException {
        MaintenanceTaskDTO task = maintenanceDao.getTaskById(maintenanceId);
        if (task == null) throw new ValidationException("Maintenance task not found.");
        if (hoursSpent <= 0) throw new ValidationException("Maintenance hours must be positive.");

        // Strategy pattern (required pattern): Shop-Tech credit math is isolated in its own strategy.
        double credit = new CreditContext(new MaintenanceCreditStrategy()).computeCredit(hoursSpent);

        maintenanceDao.completeTask(maintenanceId, hoursSpent, credit);
        if (task.getComponentId() != null) {
            maintenanceDao.resetComponentAfterMaintenance(task.getComponentId());
        }
        equipmentDao.updateStatus(task.getAssetTag(), EquipmentDTO.Status.AVAILABLE);

        AccountTransactionDTO creditTx = new AccountTransactionDTO();
        creditTx.setUserId(shopTechId);
        creditTx.setTransactionType(AccountTransactionDTO.TransactionType.CREDIT);
        creditTx.setActivityType(AccountTransactionDTO.ActivityType.MAINTENANCE);
        creditTx.setAmount(credit);
        creditTx.setDescription("Maintenance on " + task.getAssetTag() + " (" + hoursSpent + " hrs)");
        ledgerDao.recordTransaction(creditTx);
    }
}
