package dataaccesslayer;

import java.util.List;
import transferobjects.ConsumableDTO;
import transferobjects.EquipmentDTO;

/**
 * Data Access Object (DAO) interface for Equipment entities.
 * Provides methods for CRUD operations and status updates on equipment.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */

public interface EquipmentDao {
    List<EquipmentDTO> getAllEquipment();
    List<EquipmentDTO> getActiveEquipment();
    EquipmentDTO getEquipmentByAssetTag(String assetTag);
    void addEquipment(EquipmentDTO equipment);
    void updateEquipment(EquipmentDTO equipment);
    void updateStatus(String assetTag, EquipmentDTO.Status status);
    void deleteEquipment(String assetTag);
    void addUsageHours(String assetTag, double hours);

    /** FR-02 "Consumable type": the consumable(s) a piece of equipment is registered to use. */
    List<ConsumableDTO> getConsumablesForEquipment(String assetTag);
    /** Replaces the full set of consumable types linked to a piece of equipment. */
    void setEquipmentConsumables(String assetTag, List<Integer> consumableIds);
}
