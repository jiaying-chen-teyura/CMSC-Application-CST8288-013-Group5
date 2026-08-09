package transferobjects;

import java.time.LocalDateTime;

public class EquipmentComponentDTO {

    public enum ComponentStatus { HEALTHY, MAINTENANCE_REQUIRED, BROKEN }

    private Integer componentId;
    private String assetTag;
    private String componentName;
    private double usageHours;
    private double maintenanceThresholdHours;
    private ComponentStatus componentStatus = ComponentStatus.HEALTHY;
    private LocalDateTime lastMaintainedAt;

    public Integer getComponentId() { return componentId; }
    public void setComponentId(Integer componentId) { this.componentId = componentId; }

    public String getAssetTag() { return assetTag; }
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }

    public String getComponentName() { return componentName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }

    public double getUsageHours() { return usageHours; }
    public void setUsageHours(double usageHours) { this.usageHours = usageHours; }

    public double getMaintenanceThresholdHours() { return maintenanceThresholdHours; }
    public void setMaintenanceThresholdHours(double maintenanceThresholdHours) { this.maintenanceThresholdHours = maintenanceThresholdHours; }

    public ComponentStatus getComponentStatus() { return componentStatus; }
    public void setComponentStatus(ComponentStatus componentStatus) { this.componentStatus = componentStatus; }

    public LocalDateTime getLastMaintainedAt() { return lastMaintainedAt; }
    public void setLastMaintainedAt(LocalDateTime lastMaintainedAt) { this.lastMaintainedAt = lastMaintainedAt; }

    /** Fraction of threshold consumed, used for the predictive-maintenance dashboard (FR-05). */
    public double getWearFraction() {
        return maintenanceThresholdHours <= 0 ? 0 : usageHours / maintenanceThresholdHours;
    }
}
