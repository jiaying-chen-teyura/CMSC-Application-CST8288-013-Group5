package businesslayer.observer;

/** 
 * Observer Pattern (required pattern) - observer interface for FR-04 low-stock alerts. 
 * @author Le Bao Thach Nguyen 
 */
public interface InventoryListener {
    void onLowStock(InventoryAlertEvent event);
}
