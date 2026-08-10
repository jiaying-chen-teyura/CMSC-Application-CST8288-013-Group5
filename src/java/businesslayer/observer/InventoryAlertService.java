package businesslayer.observer;

import java.util.ArrayList;
import java.util.List;

/** Observer Pattern (required pattern) - the Subject/Observable for FR-04 low-stock alerts. */
public class InventoryAlertService {

    private static final InventoryAlertService INSTANCE = new InventoryAlertService();

    private final List<InventoryListener> listeners = new ArrayList<>();

    private InventoryAlertService() { }

    public static InventoryAlertService getInstance() {
        return INSTANCE;
    }

    public synchronized void addListener(InventoryListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public synchronized void removeListener(InventoryListener listener) {
        listeners.remove(listener);
    }

    public synchronized void notifyLowStock(InventoryAlertEvent event) {
        for (InventoryListener l : listeners) {
            l.onLowStock(event);
        }
    }
}
