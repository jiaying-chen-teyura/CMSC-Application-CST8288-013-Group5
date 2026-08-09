package dataaccesslayer;

import java.util.List;
import transferobjects.EquipmentUsageSessionDTO;

public interface EquipmentUsageSessionDao {
    int checkIn(EquipmentUsageSessionDTO session);
    EquipmentUsageSessionDTO getActiveSessionForEquipment(String assetTag);
    EquipmentUsageSessionDTO getSessionById(int usageSessionId);
    void checkOut(int usageSessionId, java.time.LocalDateTime checkOutTime, int elapsedMinutes, double equipmentDebit);
    List<EquipmentUsageSessionDTO> getActiveSessions();
    List<EquipmentUsageSessionDTO> getSessionsForUser(int userId);
}
