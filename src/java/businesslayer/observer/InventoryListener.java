package businesslayer.observer;

/** Observer Pattern (required pattern) - observer interface for FR-04 low-stock alerts. */
public interface InventoryListener {
    void onLowStock(InventoryAlertEvent event);
}
