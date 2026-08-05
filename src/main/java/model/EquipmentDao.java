package model;

import java.util.ArrayList;
import java.util.List;

/** Hard-coded stand-in DAO. Shared by BookEquipmentServlet (User booking)
 *  and EquipmentManagementServlet (Shop-Tech register/remove) since both
 *  act on the same equipment records. */
public class EquipmentDao {

    private static final List<Equipment> EQUIPMENT = new ArrayList<>(List.of(
        new Equipment("PR-001", "Prusa MK4",        "3D Printer",  "Available"),
        new Equipment("PR-002", "Bambu Lab X1C",     "3D Printer",  "In-Use"),
        new Equipment("LC-001", "Glowforge Pro",     "Laser Cutter","Available"),
        new Equipment("CNC-001","Shapeoko 4",        "CNC",         "Down")
    ));

    public List<Equipment> getAllEquipment() {
        return EQUIPMENT;
    }

    public boolean bookEquipment(String assetTag) {
        for (Equipment e : EQUIPMENT) {
            if (e.getAssetTag().equals(assetTag) && "Available".equals(e.getStatus())) {
                e.setStatus("In-Use");
                return true;
            }
        }
        return false;
    }

    public void registerEquipment(String assetTag, String name, String category) {
        EQUIPMENT.add(new Equipment(assetTag, name, category, "Available"));
    }

    public void deleteEquipment(String assetTag) {
        EQUIPMENT.removeIf(e -> e.getAssetTag().equals(assetTag));
    }
}
