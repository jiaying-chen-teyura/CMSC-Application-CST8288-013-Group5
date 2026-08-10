package dataaccesslayer;

import java.util.List;
import transferobjects.EquipmentUsageSessionDTO;

/**
 * Data Access Object (DAO) interface for Equipment Usage Session entities.
 * Provides methods for managing equipment usage sessions, including check-in, check-out, and retrieval of session records.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */

public interface EquipmentUsageSessionDao {
    int checkIn(EquipmentUsageSessionDTO session);
    EquipmentUsageSessionDTO getActiveSessionForEquipment(String assetTag);
    EquipmentUsageSessionDTO getSessionById(int usageSessionId);
    void checkOut(int usageSessionId, java.time.LocalDateTime checkOutTime, int elapsedMinutes, double equipmentDebit);
    List<EquipmentUsageSessionDTO> getActiveSessions();
    List<EquipmentUsageSessionDTO> getSessionsForUser(int userId);
}
