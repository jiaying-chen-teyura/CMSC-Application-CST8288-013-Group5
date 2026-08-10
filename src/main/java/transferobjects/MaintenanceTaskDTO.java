package transferobjects;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for Maintenance Task entities.
 * Represents the data structure for maintenance tasks, including maintenance ID, asset tag, component ID, assigned shop technician ID, maintenance type, description, priority, scheduled start time, actual start and completion times, maintenance hours, status, credit earned, and convenience fields for equipment and component names.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */

public class MaintenanceTaskDTO {

    public enum MaintenanceType { PREVENTIVE, REPAIR, INSPECTION }
    public enum Priority { LOW, MEDIUM, HIGH, URGENT }
    public enum Status { ALERTED, SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED }

    private Integer maintenanceId;
    private String assetTag;
    private Integer componentId;
    private Integer assignedShopTechId;
    private MaintenanceType maintenanceType;
    private String description;
    private Priority priority = Priority.MEDIUM;
    private LocalDateTime scheduledStart;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Double maintenanceHours;
    private Status status = Status.ALERTED;
    private double creditEarned;

    // convenience, filled by business layer for reporting screens
    private String equipmentName;
    private String componentName;

    public Integer getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(Integer maintenanceId) { this.maintenanceId = maintenanceId; }

    public String getAssetTag() { return assetTag; }
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }

    public Integer getComponentId() { return componentId; }
    public void setComponentId(Integer componentId) { this.componentId = componentId; }

    public Integer getAssignedShopTechId() { return assignedShopTechId; }
    public void setAssignedShopTechId(Integer assignedShopTechId) { this.assignedShopTechId = assignedShopTechId; }

    public MaintenanceType getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(MaintenanceType maintenanceType) { this.maintenanceType = maintenanceType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public LocalDateTime getScheduledStart() { return scheduledStart; }
    public void setScheduledStart(LocalDateTime scheduledStart) { this.scheduledStart = scheduledStart; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public Double getMaintenanceHours() { return maintenanceHours; }
    public void setMaintenanceHours(Double maintenanceHours) { this.maintenanceHours = maintenanceHours; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public double getCreditEarned() { return creditEarned; }
    public void setCreditEarned(double creditEarned) { this.creditEarned = creditEarned; }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public String getComponentName() { return componentName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }
}
