package dataaccesslayer;

import java.util.List;
import transferobjects.EquipmentDTO;

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
