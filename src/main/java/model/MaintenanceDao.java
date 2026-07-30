package model;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceDao {

    private static final List<MaintenanceAlert> ALERTS = new ArrayList<>(List.of(
        new MaintenanceAlert("Bambu Lab X1C", "Nozzle wear > 200 print hours", "Open"),
        new MaintenanceAlert("Shapeoko 4",    "Spindle diagnostics failed",     "Open")
    ));

    public List<MaintenanceAlert> getAllAlerts() {
        return ALERTS;
    }

    public boolean scheduleMaintenance(String equipmentName) {
        for (MaintenanceAlert a : ALERTS) {
            if (a.getEquipmentName().equals(equipmentName)) {
                a.setStatus("Scheduled");
                return true;
            }
        }
        return false;
    }
}
