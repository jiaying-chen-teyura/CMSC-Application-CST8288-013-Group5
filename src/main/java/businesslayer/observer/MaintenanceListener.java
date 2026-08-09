package businesslayer.observer;

/**
 * Observer Pattern (required pattern) - observer interface for FR-05 predictive maintenance alerts.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */

/** Observer Pattern (required pattern) - observer interface for FR-05 predictive maintenance alerts. */
public interface MaintenanceListener {
    void onMaintenanceAlert(MaintenanceAlertEvent event);
}
