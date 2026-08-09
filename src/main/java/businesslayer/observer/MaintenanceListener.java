package businesslayer.observer;

/** Observer Pattern (required pattern) - observer interface for FR-05 predictive maintenance alerts. */
public interface MaintenanceListener {
    void onMaintenanceAlert(MaintenanceAlertEvent event);
}
