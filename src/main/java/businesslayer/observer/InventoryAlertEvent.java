package businesslayer.observer;

/** Immutable event payload broadcast to observers when a consumable falls to/below its restock level. */
public class InventoryAlertEvent {
    private final int consumableId;
    private final String materialName;
    private final double currentStock;
    private final double restockLevel;

    public InventoryAlertEvent(int consumableId, String materialName, double currentStock, double restockLevel) {
        this.consumableId = consumableId;
        this.materialName = materialName;
        this.currentStock = currentStock;
        this.restockLevel = restockLevel;
    }

    public int getConsumableId() { return consumableId; }
    public String getMaterialName() { return materialName; }
    public double getCurrentStock() { return currentStock; }
    public double getRestockLevel() { return restockLevel; }
}
