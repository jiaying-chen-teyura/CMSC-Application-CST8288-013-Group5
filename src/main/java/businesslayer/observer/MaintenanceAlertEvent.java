package businesslayer.observer;

/** Immutable event payload broadcast to observers when a component crosses its wear threshold. */
public class MaintenanceAlertEvent {
    private final String assetTag;
    private final String equipmentName;
    private final Integer componentId;
    private final String componentName;
    private final double usageHours;
    private final double thresholdHours;

    public MaintenanceAlertEvent(String assetTag, String equipmentName, Integer componentId,
                                  String componentName, double usageHours, double thresholdHours) {
        this.assetTag = assetTag;
        this.equipmentName = equipmentName;
        this.componentId = componentId;
        this.componentName = componentName;
        this.usageHours = usageHours;
        this.thresholdHours = thresholdHours;
    }

    public String getAssetTag() { return assetTag; }
    public String getEquipmentName() { return equipmentName; }
    public Integer getComponentId() { return componentId; }
    public String getComponentName() { return componentName; }
    public double getUsageHours() { return usageHours; }
    public double getThresholdHours() { return thresholdHours; }
}
