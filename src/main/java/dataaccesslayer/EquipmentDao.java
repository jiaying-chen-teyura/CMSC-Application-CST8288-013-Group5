package dataaccesslayer;

import java.util.List;
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
}
