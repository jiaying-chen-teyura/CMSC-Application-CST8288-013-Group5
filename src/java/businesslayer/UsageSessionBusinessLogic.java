package businesslayer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import businesslayer.observer.*;
import dataaccesslayer.*;
import transferobjects.*;

/**
 * Backs FR-03 (Usage & Session Tracking): equipment check-in/check-out,
 * the live "who is using what" report, and the debit side of the credit
 * ledger (equipment access hours + materials consumed).
 *
 * checkOut(...) is also where FR-05 gets triggered: every session wears the
 * equipment's components (MaintenanceBusinessLogic.evaluateWear), and if
 * that pushes a component over its maintenance threshold the Observer
 * pattern (MaintenanceAlertService -> ShopTechAlertListener) fires and a
 * predictive-maintenance alert is opened for the Shop-Techs to schedule.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */
public class UsageSessionBusinessLogic {

    private final EquipmentUsageSessionDao sessionDao;
    private final EquipmentDao equipmentDao;
    private final EquipmentBookingDao bookingDao;
    private final ConsumableDao consumableDao;
    private final MaintenanceDao maintenanceDao;
    private final LedgerDao ledgerDao;
    private final MaintenanceBusinessLogic maintenanceBL;

    public UsageSessionBusinessLogic() {
        this(new EquipmentUsageSessionDaoImpl(), new EquipmentDaoImpl(), new EquipmentBookingDaoImpl(),
             new ConsumableDaoImpl(), new MaintenanceDaoImpl(), new LedgerDaoImpl());
    }

    /**
     * Constructor for dependency injection, primarily for unit testing.
     * @param sessionDao the EquipmentUsageSessionDao to use
     * @param equipmentDao the EquipmentDao to use
     * @param bookingDao the EquipmentBookingDao to use
     * @param consumableDao the ConsumableDao to use
     * @param maintenanceDao the MaintenanceDao to use
     * @param ledgerDao the LedgerDao to use
     */
    public UsageSessionBusinessLogic(EquipmentUsageSessionDao sessionDao, EquipmentDao equipmentDao,
                                      EquipmentBookingDao bookingDao, ConsumableDao consumableDao,
                                      MaintenanceDao maintenanceDao, LedgerDao ledgerDao) {
        this.sessionDao = sessionDao;
        this.equipmentDao = equipmentDao;
        this.bookingDao = bookingDao;
        this.consumableDao = consumableDao;
        this.maintenanceDao = maintenanceDao;
        this.ledgerDao = ledgerDao;
        // Composition, not duplication: predictive-maintenance rules (FR-05) live in one place.
        this.maintenanceBL = new MaintenanceBusinessLogic(maintenanceDao, equipmentDao, ledgerDao);
    }

    /** A single "I used N units of consumable X" line item reported at check-out time. */
    public static class MaterialUsageRequest {
        public final int consumableId;
        public final double quantity;
        public MaterialUsageRequest(int consumableId, double quantity) {
            this.consumableId = consumableId;
            this.quantity = quantity;
        }
    }

    /**
     * Checks in a user to an equipment usage session, creating a new session record.
     * @param userId the ID of the user checking in
     * @param assetTag the asset tag of the equipment being checked into
     * @param bookingId the ID of the associated booking, if any (can be null)
     * @return the newly created EquipmentUsageSessionDTO
     * @throws ValidationException if validation fails (e.g., equipment doesn't exist, is inactive, or already has an active session)
     */
    public EquipmentUsageSessionDTO checkIn(int userId, String assetTag, Integer bookingId) throws ValidationException {
        EquipmentDTO equipment = equipmentDao.getEquipmentByAssetTag(assetTag);
        if (equipment == null || !equipment.isActive()) {
            throw new ValidationException("That equipment does not exist or is inactive.");
        }
        if (equipment.getStatus() == EquipmentDTO.Status.MAINTENANCE || equipment.getStatus() == EquipmentDTO.Status.UNAVAILABLE) {
            throw new ValidationException("That equipment is currently " + equipment.getStatus().name().toLowerCase() + " and cannot be checked into.");
        }
        if (sessionDao.getActiveSessionForEquipment(assetTag) != null) {
            throw new ValidationException("That equipment already has an active session.");
        }

        EquipmentUsageSessionDTO session = new EquipmentUsageSessionDTO();
        session.setUserId(userId);
        session.setAssetTag(assetTag);
        session.setBookingId(bookingId);
        session.setCheckInTime(LocalDateTime.now());
        session.setHourlyRate(equipment.getAccessCreditRate());
        int id = sessionDao.checkIn(session);
        session.setUsageSessionId(id);

        equipmentDao.updateStatus(assetTag, EquipmentDTO.Status.IN_USE);
        if (bookingId != null) {
            // The booking isn't done yet - it's only "in progress" until the member checks out.
            bookingDao.updateStatus(bookingId, EquipmentBookingDTO.BookingStatus.IN_PROGRESS);
        }
        return session;
    }

    /**
     * Checks out a user from an equipment usage session, finalizing the session record and applying any material usage.
     * @param usageSessionId the ID of the usage session to check out from
     * @param requestingUserId the ID of the user requesting the check-out (must match the session's user)
     * @param materialsUsed a list of MaterialUsageRequest representing any consumables used during the session
     * @throws ValidationException if validation fails (e.g., session not found, already closed, or user mismatch)
     */
    public void checkOut(int usageSessionId, int requestingUserId, List<MaterialUsageRequest> materialsUsed)
            throws ValidationException {
        EquipmentUsageSessionDTO session = sessionDao.getSessionById(usageSessionId);
        if (session == null) throw new ValidationException("Session not found.");
        if (session.getSessionStatus() != EquipmentUsageSessionDTO.SessionStatus.ACTIVE) {
            throw new ValidationException("That session is already closed.");
        }
        if (session.getUserId() != requestingUserId) {
            throw new ValidationException("You can only check out your own active session.");
        }

        LocalDateTime checkOutTime = LocalDateTime.now();
        long minutes = Math.max(1, Duration.between(session.getCheckInTime(), checkOutTime).toMinutes());
        double hours = minutes / 60.0;
        double equipmentDebit = round2(hours * session.getHourlyRate());

        sessionDao.checkOut(usageSessionId, checkOutTime, (int) minutes, equipmentDebit);
        equipmentDao.updateStatus(session.getAssetTag(), EquipmentDTO.Status.AVAILABLE);
        equipmentDao.addUsageHours(session.getAssetTag(), hours);
        if (session.getBookingId() != null) {
            // The booking is only now complete - the member has finished using the equipment.
            bookingDao.updateStatus(session.getBookingId(), EquipmentBookingDTO.BookingStatus.COMPLETED);
        }

        // debit for equipment access hours (Credits/Debits definition in the assignment)
        recordDebit(session.getUserId(), AccountTransactionDTO.ActivityType.EQUIPMENT_USAGE, equipmentDebit,
                "Equipment access: " + session.getAssetTag() + " (" + minutes + " min)");

        if (materialsUsed != null) {
            for (MaterialUsageRequest req : materialsUsed) {
                applyMaterialUsage(session, req);
            }
        }

        wearComponentsForSession(session.getAssetTag(), hours);
    }

    /**
     * Applies a material usage request to a session, adjusting stock and recording the debit.
     * @param session the EquipmentUsageSessionDTO representing the active session
     * @param req the MaterialUsageRequest representing the consumable usage
     * @throws ValidationException if validation fails (e.g., unknown consumable, insufficient stock)
     */
    private void applyMaterialUsage(EquipmentUsageSessionDTO session, MaterialUsageRequest req) throws ValidationException {
        ConsumableDTO consumable = consumableDao.getConsumableById(req.consumableId);
        if (consumable == null) throw new ValidationException("Unknown consumable.");
        if (req.quantity <= 0) throw new ValidationException("Material quantity must be positive.");
        if (req.quantity > consumable.getCurrentStock()) {
            throw new ValidationException("Not enough " + consumable.getMaterialName() + " in stock.");
        }

        double debit = round2(req.quantity * consumable.getUnitDebitRate());

        MaterialUsageDTO usage = new MaterialUsageDTO();
        usage.setUsageSessionId(session.getUsageSessionId());
        usage.setConsumableId(req.consumableId);
        usage.setQuantityUsed(req.quantity);
        usage.setUnitRate(consumable.getUnitDebitRate());
        usage.setMaterialDebit(debit);
        consumableDao.recordMaterialUsage(usage);
        consumableDao.adjustStock(req.consumableId, -req.quantity);

        InventoryTransactionDTO tx = new InventoryTransactionDTO();
        tx.setConsumableId(req.consumableId);
        tx.setTransactionType(InventoryTransactionDTO.TransactionType.USAGE);
        tx.setQuantityChange(-req.quantity);
        tx.setPerformedBy(session.getUserId());
        tx.setNotes("Consumed during usage session #" + session.getUsageSessionId());
        consumableDao.recordInventoryTransaction(tx);

        recordDebit(session.getUserId(), AccountTransactionDTO.ActivityType.MATERIAL_USAGE, debit,
                "Material: " + consumable.getMaterialName() + " x" + req.quantity);

        // Observer pattern: notify Shop-Techs the moment stock dips to/below the restock level (FR-04).
        double newStock = consumable.getCurrentStock() - req.quantity;
        if (newStock <= consumable.getRestockLevel()) {
            InventoryAlertService.getInstance().notifyLowStock(new InventoryAlertEvent(
                    req.consumableId, consumable.getMaterialName(), newStock, consumable.getRestockLevel()));
        }
    }

    /**
     * Applies wear to all components of the equipment based on usage hours, and delegates to
     * MaintenanceBusinessLogic.evaluateWear (FR-05) to raise alerts / open tasks / flip the
     * equipment UNAVAILABLE as needed.
     * @param assetTag the asset tag of the equipment
     * @param hours the number of hours the equipment was used
     */
    private void wearComponentsForSession(String assetTag, double hours) {
        List<EquipmentComponentDTO> components = maintenanceDao.getComponentsForEquipment(assetTag);
        EquipmentDTO equipment = equipmentDao.getEquipmentByAssetTag(assetTag);
        for (EquipmentComponentDTO c : components) {
            maintenanceDao.addWearHours(c.getComponentId(), hours);
            double newHours = c.getUsageHours() + hours;
            maintenanceBL.evaluateWear(c, equipment, newHours);
        }
    }

    /**
     * Records a debit transaction in the ledger for a user.
     * @param userId the ID of the user
     * @param activityType the type of activity (equipment usage or material usage)
     * @param amount the amount to debit
     * @param description a description of the transaction
     */
    private void recordDebit(int userId, AccountTransactionDTO.ActivityType activityType, double amount, String description) {
        if (amount <= 0) return;
        AccountTransactionDTO tx = new AccountTransactionDTO();
        tx.setUserId(userId);
        tx.setTransactionType(AccountTransactionDTO.TransactionType.DEBIT);
        tx.setActivityType(activityType);
        tx.setAmount(amount);
        tx.setDescription(description);
        ledgerDao.recordTransaction(tx);
    }

    /**
     * Retrieves all active equipment usage sessions, enriched for the FR-03 live session report:
     * a live elapsed-time figure (computed from now, since elapsed_minutes is only persisted at
     * check-out), the materials reported against the session so far, and the consumable types
     * this equipment accepts (FR-02) so the check-out form only offers relevant materials.
     * @return a list of EquipmentUsageSessionDTOs representing all active sessions
     */
    public List<EquipmentUsageSessionDTO> getActiveSessions() {
        List<EquipmentUsageSessionDTO> sessions = sessionDao.getActiveSessions();
        for (EquipmentUsageSessionDTO s : sessions) {
            s.setElapsedMinutes((int) Duration.between(s.getCheckInTime(), LocalDateTime.now()).toMinutes());
            s.setMaterialsUsed(consumableDao.getMaterialUsageForSession(s.getUsageSessionId()));
            s.setAvailableConsumables(equipmentDao.getConsumablesForEquipment(s.getAssetTag()));
        }
        return sessions;
    }

    /**
     * Convenience for the Booking screen: stamps each of the caller's own
     * bookings with the usageSessionId of its matching ACTIVE session (if
     * any), so the "My Bookings" row can show a Check Out button directly
     * instead of the member having to search the separate session report
     * for it.
     */
    public void attachActiveSessionIds(List<EquipmentBookingDTO> bookings, List<EquipmentUsageSessionDTO> activeSessions) {
        for (EquipmentBookingDTO booking : bookings) {
            for (EquipmentUsageSessionDTO session : activeSessions) {
                if (session.getBookingId() != null && session.getBookingId().equals(booking.getBookingId())) {
                    booking.setActiveUsageSessionId(session.getUsageSessionId());
                    break;
                }
            }
        }
    }

    /**
     * Retrieves all usage sessions for a specific user.
     * @param userId the ID of the user
     * @return a list of EquipmentUsageSessionDTOs representing the user's sessions
     */
    public List<EquipmentUsageSessionDTO> getSessionsForUser(int userId) {
        return sessionDao.getSessionsForUser(userId);
    }

    /**
     * Rounds a double value to two decimal places.
     * @param v the value to round
     * @return the rounded value
     */
    private double round2(double v) { return Math.round(v * 100.0) / 100.0; }
}
