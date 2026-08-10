package transferobjects;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for Equipment entities.
 * Represents the data structure for equipment, including asset tag, make, model, category, status, access credit rate, total usage hours, location, and registration details.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */

public class EquipmentComponentDTO {

    public enum ComponentStatus { HEALTHY, MAINTENANCE_REQUIRED, BROKEN }

    /**
     * Design decision (documented in the High Level Design per the assignment's
     * "functional requirements are intentionally left open" note): a component
     * gets its predictive-maintenance ALERT at {@code maintenanceThresholdHours},
     * but the equipment does not go UNAVAILABLE the moment that alert fires -
     * the Shop-Tech has a grace window to schedule and complete the work.
     * If wear keeps accumulating past 125% of the alert threshold with the
     * maintenance still not completed, the equipment is forced UNAVAILABLE.
     */
    public static final double UNAVAILABLE_THRESHOLD_MULTIPLIER = 1.25;

    private Integer componentId;
    private String assetTag;
    private String componentName;
    private double usageHours;
    private double maintenanceThresholdHours;
    private ComponentStatus componentStatus = ComponentStatus.HEALTHY;
    private LocalDateTime lastMaintainedAt;

    /** Default constructor for EquipmentComponentDTO. */
    public Integer getComponentId() { return componentId; }
    public void setComponentId(Integer componentId) { this.componentId = componentId; }

    /** Getter and setter methods for the EquipmentComponentDTO fields. */
    public String getAssetTag() { return assetTag; }
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }

    /** Getter and setter methods for the EquipmentComponentDTO fields. */
    public String getComponentName() { return componentName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }

    /** Getter and setter methods for the EquipmentComponentDTO fields. */
    public double getUsageHours() { return usageHours; }
    public void setUsageHours(double usageHours) { this.usageHours = usageHours; }

    public double getMaintenanceThresholdHours() { return maintenanceThresholdHours; }
    public void setMaintenanceThresholdHours(double maintenanceThresholdHours) { this.maintenanceThresholdHours = maintenanceThresholdHours; }

    public ComponentStatus getComponentStatus() { return componentStatus; }
    public void setComponentStatus(ComponentStatus componentStatus) { this.componentStatus = componentStatus; }

    public LocalDateTime getLastMaintainedAt() { return lastMaintainedAt; }
    public void setLastMaintainedAt(LocalDateTime lastMaintainedAt) { this.lastMaintainedAt = lastMaintainedAt; }

    /** Fraction of threshold consumed, used for the predictive-maintenance dashboard (FR-05). */
    /** Returns a value between 0 and 1, where 1 indicates the component has reached its maintenance threshold. */
    public double getWearFraction() {
        return maintenanceThresholdHours <= 0 ? 0 : usageHours / maintenanceThresholdHours;
    }

    /** The hard "working hours limit" (FR-05): past this, the equipment goes UNAVAILABLE if not yet serviced. */
    public double getUnavailableThresholdHours() {
        return maintenanceThresholdHours * UNAVAILABLE_THRESHOLD_MULTIPLIER;
    }
}
