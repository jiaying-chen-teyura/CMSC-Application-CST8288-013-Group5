package dataaccesslayer;

import java.util.List;
import transferobjects.EquipmentUsageSessionDTO;

/**
 * Data Access Object (DAO) interface for equipment usage session entities.
 * Provides methods for creating, updating, and retrieving equipment usage sessions.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */
public interface EquipmentUsageSessionDao {
    /**
     * Persists a new equipment usage session.
     *
     * @param session the session data to save
     * @return the generated session identifier, or -1 if none was generated
     */
    int checkIn(EquipmentUsageSessionDTO session);

    /**
     * Retrieves the currently active session for a specific equipment asset.
     *
     * @param assetTag the asset tag to look up
     * @return the active session, or null if none exists
     */
    EquipmentUsageSessionDTO getActiveSessionForEquipment(String assetTag);

    /**
     * Retrieves a usage session by its identifier.
     *
     * @param usageSessionId the session identifier to look up
     * @return the matching session, or null if not found
     */
    EquipmentUsageSessionDTO getSessionById(int usageSessionId);

    /**
     * Completes an active usage session and stores checkout details.
     *
     * @param usageSessionId the session identifier to update
     * @param checkOutTime the checkout time to record
     * @param elapsedMinutes the total elapsed session minutes
     * @param equipmentDebit the equipment debit to charge
     */
    void checkOut(int usageSessionId, java.time.LocalDateTime checkOutTime, int elapsedMinutes, double equipmentDebit);

    /**
     * Retrieves all currently active sessions.
     *
     * @return a list of active sessions
     */
    List<EquipmentUsageSessionDTO> getActiveSessions();

    /**
     * Retrieves all sessions recorded for a specific user.
     *
     * @param userId the identifier of the user
     * @return a list of sessions for the user
     */
    List<EquipmentUsageSessionDTO> getSessionsForUser(int userId);
}
