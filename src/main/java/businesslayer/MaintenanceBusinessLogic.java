package businesslayer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

/**
 * Backs FR-05 (Alerts for Predictive Maintenance) and the Shop-Tech maintenance use cases.
 * @author Oladimeji Durojaiye
 * @version 1.0
 *
 * Life cycle of a single predictive-maintenance task (mirrors equipment
 * booking -> check-in -> check-out):
 *   ALERTED    - system-raised the moment a component crosses its wear threshold; unassigned.
 *   SCHEDULED  - a Shop-Tech has claimed the alert and picked a time (scheduleMaintenance).
 *   IN_PROGRESS- the Shop-Tech has checked in to the job (startMaintenance).
 *   COMPLETED  - the Shop-Tech has checked out; hours worked are computed automatically and
 *                credited, and the component's wear clock resets to zero (performMaintenance).
 */
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

    /** Every task that is not yet finished (ALERTED, SCHEDULED, or IN_PROGRESS) - the Shop-Tech's full worklist. */
    public List<MaintenanceTaskDTO> getOpenAlerts() {
        return maintenanceDao.getOpenMaintenanceTasks();
    }

    /** Just the unclaimed alerts - these are what the Schedule Maintenance form offers, per FR-05. */
    public List<MaintenanceTaskDTO> getPendingAlerts() {
        List<MaintenanceTaskDTO> pending = new ArrayList<>();
        for (MaintenanceTaskDTO t : maintenanceDao.getOpenMaintenanceTasks()) {
            if (t.getStatus() == MaintenanceTaskDTO.Status.ALERTED) pending.add(t);
        }
        return pending;
    }

    /** Equipment that currently needs attention: has an open predictive-maintenance task, or is already UNAVAILABLE. */
    public List<EquipmentDTO> getEquipmentNeedingAttention() {
        LinkedHashMap<String, EquipmentDTO> attention = new LinkedHashMap<>();
        for (EquipmentDTO e : equipmentDao.getActiveEquipment()) {
            if (e.getStatus() == EquipmentDTO.Status.UNAVAILABLE) attention.put(e.getAssetTag(), e);
        }
        // Health status (needsMaintenance) is driven by having an open predictive-maintenance task -
        // deliberately separate from `status`, which stays AVAILABLE until a Shop-Tech actually starts
        // the work. Without this flag the JSP had nothing but `status` to show here, which made
        // equipment that's clearly flagged in the "Open Alerts" table below look perfectly healthy.
        for (MaintenanceTaskDTO t : maintenanceDao.getOpenMaintenanceTasks()) {
            EquipmentDTO existing = attention.get(t.getAssetTag());
            if (existing != null) {
                existing.setNeedsMaintenance(true);
                continue;
            }
            EquipmentDTO e = equipmentDao.getEquipmentByAssetTag(t.getAssetTag());
            if (e != null) {
                e.setNeedsMaintenance(true);
                attention.put(e.getAssetTag(), e);
            }
        }
        return new ArrayList<>(attention.values());
    }

    public List<MaintenanceTaskDTO> getTasksForShopTech(int shopTechId) {
        return maintenanceDao.getTasksForShopTech(shopTechId);
    }

    /**
     * A Shop-Tech's completed maintenance history - gives them feedback that finishing a job
     * (performMaintenance) actually did something, the same way "My Work Orders" does for work
     * orders. Most recently completed first.
     */
    public List<MaintenanceTaskDTO> getCompletedMaintenanceForShopTech(int shopTechId) {
        List<MaintenanceTaskDTO> completed = new ArrayList<>();
        for (MaintenanceTaskDTO t : maintenanceDao.getTasksForShopTech(shopTechId)) {
            if (t.getStatus() == MaintenanceTaskDTO.Status.COMPLETED) completed.add(t);
        }
        completed.sort((a, b) -> {
            if (a.getCompletedAt() == null || b.getCompletedAt() == null) return 0;
            return b.getCompletedAt().compareTo(a.getCompletedAt());
        });
        return completed;
    }

    /**
     * Central predictive-maintenance rule (FR-05), called any time a component's usage hours
     * change (equipment check-out wear, or a diagnostics reading). If usage has crossed the
     * alert threshold, flips the component to MAINTENANCE_REQUIRED, fires the Observer alert
     * the first time this happens, and opens a single ALERTED maintenance task for it (re-wear
     * does not create duplicates - resetComponentAfterMaintenance clears the slate). If usage
     * keeps climbing past the hard "working hours limit" without the task being completed, the
     * equipment itself is taken UNAVAILABLE so it can no longer be booked or checked in to.
     */
    public void evaluateWear(EquipmentComponentDTO component, EquipmentDTO equipment, double newHours) {
        boolean crossedAlert = newHours >= component.getMaintenanceThresholdHours();

        if (crossedAlert && component.getComponentStatus() == EquipmentComponentDTO.ComponentStatus.HEALTHY) {
            maintenanceDao.setComponentStatus(component.getComponentId(), EquipmentComponentDTO.ComponentStatus.MAINTENANCE_REQUIRED);

            // Observer pattern (required pattern): broadcast the predictive-maintenance alert (FR-05).
            MaintenanceAlertService.getInstance().notifyAlert(new MaintenanceAlertEvent(
                    component.getAssetTag(), equipment != null ? equipment.getEquipmentName() : component.getAssetTag(),
                    component.getComponentId(), component.getComponentName(), newHours, component.getMaintenanceThresholdHours()));
        }

        if (crossedAlert && maintenanceDao.getOpenTaskForComponent(component.getComponentId()) == null) {
            MaintenanceTaskDTO alert = new MaintenanceTaskDTO();
            alert.setAssetTag(component.getAssetTag());
            alert.setComponentId(component.getComponentId());
            alert.setMaintenanceType(MaintenanceTaskDTO.MaintenanceType.PREVENTIVE);
            alert.setDescription(String.format("%s reached its predictive-maintenance alert threshold (%.1f / %.1f hrs).",
                    component.getComponentName(), newHours, component.getMaintenanceThresholdHours()));
            alert.setPriority(MaintenanceTaskDTO.Priority.MEDIUM);
            // status defaults to ALERTED - unassigned until a Shop-Tech schedules it.
            maintenanceDao.createMaintenanceTask(alert);
        }

        if (newHours >= component.getUnavailableThresholdHours()) {
            equipmentDao.updateStatus(component.getAssetTag(), EquipmentDTO.Status.UNAVAILABLE);
        }
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
        EquipmentDTO equipment = equipmentDao.getEquipmentByAssetTag(reading.getAssetTag());
        List<EquipmentComponentDTO> components = maintenanceDao.getComponentsForEquipment(reading.getAssetTag());
        for (EquipmentComponentDTO c : components) {
            if (!c.getComponentName().equalsIgnoreCase(reading.getComponentName())) continue;
            maintenanceDao.addWearHours(c.getComponentId(), reading.getAdditionalUsageHours());
            double newHours = c.getUsageHours() + reading.getAdditionalUsageHours();
            evaluateWear(c, equipment, newHours);
        }
    }

    /**
     * A Shop-Tech claims an ALERTED alert: picks a scheduled time and (optionally) refines the
     * type/priority/description. The equipment stays in service until the Shop-Tech actually
     * starts the work - only the alert record itself moves, from ALERTED to SCHEDULED.
     */
    public MaintenanceTaskDTO scheduleMaintenance(int maintenanceId, int shopTechId, MaintenanceTaskDTO.MaintenanceType type,
                                                    String description, MaintenanceTaskDTO.Priority priority,
                                                    LocalDateTime scheduledStart) throws ValidationException {
        MaintenanceTaskDTO task = maintenanceDao.getTaskById(maintenanceId);
        if (task == null) throw new ValidationException("That maintenance alert no longer exists.");
        if (task.getStatus() != MaintenanceTaskDTO.Status.ALERTED) {
            throw new ValidationException("That alert has already been scheduled or completed by another Shop-Tech.");
        }
        if (description == null || description.isBlank()) throw new ValidationException("Description is required.");
        if (scheduledStart == null) throw new ValidationException("Scheduled start time is required.");
        TimeSlotValidation.validateQuarterHourSlot(scheduledStart, "Scheduled start");

        maintenanceDao.scheduleTask(maintenanceId, shopTechId, scheduledStart, type, priority, description);
        task.setAssignedShopTechId(shopTechId);
        task.setMaintenanceType(type);
        task.setDescription(description);
        task.setPriority(priority);
        task.setScheduledStart(scheduledStart);
        task.setStatus(MaintenanceTaskDTO.Status.SCHEDULED);
        return task;
    }

    /** The Shop-Tech "checks in" to a scheduled job - starts the maintenance-hours clock. */
    public void startMaintenance(int maintenanceId, int shopTechId) throws ValidationException {
        MaintenanceTaskDTO task = maintenanceDao.getTaskById(maintenanceId);
        if (task == null) throw new ValidationException("Maintenance task not found.");
        if (task.getAssignedShopTechId() == null || task.getAssignedShopTechId() != shopTechId) {
            throw new ValidationException("This task is assigned to a different Shop-Tech.");
        }
        if (task.getStatus() != MaintenanceTaskDTO.Status.SCHEDULED) {
            throw new ValidationException("Only a scheduled task can be started.");
        }
        maintenanceDao.startTask(maintenanceId);
        equipmentDao.updateStatus(task.getAssetTag(), EquipmentDTO.Status.MAINTENANCE);
    }

    /**
     * The Shop-Tech "checks out" of the job: maintenance hours are the elapsed time since
     * startMaintenance (just like the equipment usage-session check-out), credit is computed
     * from those hours (Strategy pattern), the component's wear clock resets, and the equipment
     * comes back AVAILABLE.
     */
    public void performMaintenance(int maintenanceId, int shopTechId) throws ValidationException {
        MaintenanceTaskDTO task = maintenanceDao.getTaskById(maintenanceId);
        if (task == null) throw new ValidationException("Maintenance task not found.");
        if (task.getAssignedShopTechId() == null || task.getAssignedShopTechId() != shopTechId) {
            throw new ValidationException("This task is assigned to a different Shop-Tech.");
        }
        if (task.getStatus() != MaintenanceTaskDTO.Status.IN_PROGRESS) {
            throw new ValidationException("Start the maintenance task before completing it.");
        }

        long minutes = Math.max(1, Duration.between(task.getStartedAt(), LocalDateTime.now()).toMinutes());
        double hoursSpent = minutes / 60.0;

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
        creditTx.setDescription("Maintenance on " + task.getAssetTag() + " (" + String.format("%.2f", hoursSpent) + " hrs)");
        ledgerDao.recordTransaction(creditTx);
    }
}
