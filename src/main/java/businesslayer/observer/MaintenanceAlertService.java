package businesslayer.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern (required pattern) - the Subject/Observable.
 * MaintenanceBusinessLogic calls notifyAlert(...) whenever a component's
 * usage_hours crosses maintenance_threshold_hours (FR-05); every registered
 * listener (Shop-Tech notifications, equipment status flip, etc.) reacts
 * independently without MaintenanceBusinessLogic knowing who's listening.
 * A single static instance is used app-wide so listeners registered once
 * (e.g. at servlet init) stay attached for the life of the application.
 */
public class MaintenanceAlertService {

    private static final MaintenanceAlertService INSTANCE = new MaintenanceAlertService();

    private final List<MaintenanceListener> listeners = new ArrayList<>();

    private MaintenanceAlertService() { }

    public static MaintenanceAlertService getInstance() {
        return INSTANCE;
    }

    public synchronized void addListener(MaintenanceListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public synchronized void removeListener(MaintenanceListener listener) {
        listeners.remove(listener);
    }

    public synchronized void notifyAlert(MaintenanceAlertEvent event) {
        for (MaintenanceListener l : listeners) {
            l.onMaintenanceAlert(event);
        }
    }
}
