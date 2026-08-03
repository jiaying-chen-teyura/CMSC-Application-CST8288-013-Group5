package service;

import dao.EquipmentDao;
import dao.EquipmentDaoImpl;
import dto.EquipmentCategory;
import dto.EquipmentDTO;
import dto.EquipmentStatus;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Business layer for the Equipment module (FR-02 Equipment Registration,
 * FR-03 real-time status). Named to match the "Equipment Service" box in
 * the HLD's Architecture Diagram (Section 5.2, Business Layer).
 * <p>
 * Enforces the validation rules the DAO/database can't (or shouldn't)
 * enforce on their own, then delegates persistence to {@link EquipmentDao}.
 * Servlets call only this class, never {@link dao.EquipmentDaoImpl}
 * directly — that boundary is what makes this a genuine layered
 * architecture rather than a Controller with extra steps.
 *
 * @author Oladimeji Durojaiye
 * @version 1.0
 */
public class EquipmentService {

    private final EquipmentDao equipmentDao;

    /** Default constructor — wires the real JDBC DAO implementation. */
    public EquipmentService() {
        this.equipmentDao = new EquipmentDaoImpl();
    }

    /**
     * Constructor for dependency injection (e.g. a test supplying a mock
     * {@link EquipmentDao} instead of {@link EquipmentDaoImpl}, without
     * touching a real database).
     *
     * @param equipmentDao the DAO implementation to use
     */
    public EquipmentService(EquipmentDao equipmentDao) {
        this.equipmentDao = equipmentDao;
    }

    /**
     * Validates and registers a new piece of equipment.
     * <p>
     * Rules enforced here (the CHECK constraints in {@code CMSC_database.sql}
     * are the last line of defense, not the first — a bad row should never
     * get far enough to hit them):
     * <ul>
     *   <li>{@code assetTag}, {@code make}, {@code model}, {@code category},
     *       and {@code equipmentName} are required.</li>
     *   <li>{@code accessCreditRate} is required and must be {@code >= 0}
     *       (mirrors {@code chk_equipment_access_rate}).</li>
     *   <li>{@code assetTag} must not already exist (the table's primary
     *       key would reject a duplicate anyway, but this returns a clean
     *       message instead of a raw {@link SQLException} bubbling up).</li>
     * </ul>
     * On success, {@code status} defaults to {@link EquipmentStatus#AVAILABLE}
     * and {@code totalUsageHours} to {@link BigDecimal#ZERO} before the
     * insert, matching a freshly registered machine that has never been used.
     *
     * @param equipment the equipment to register (registeredBy must already
     *                   be set to the calling Shop-Tech/admin's user_id)
     * @return a success result carrying the persisted equipment, or a
     *         failure result carrying a validation message
     * @throws SQLException if a database error occurs
     */
    public EquipmentResult registerEquipment(EquipmentDTO equipment) throws SQLException {
        String validationError = validateForRegistration(equipment);
        if (validationError != null) {
            return EquipmentResult.failure(validationError);
        }

        if (equipmentDao.findByAssetTag(equipment.getAssetTag()) != null) {
            return EquipmentResult.failure(
                    "Asset tag " + equipment.getAssetTag() + " is already in use.");
        }

        equipment.setStatus(EquipmentStatus.AVAILABLE);
        equipment.setTotalUsageHours(BigDecimal.ZERO);

        boolean inserted = equipmentDao.insert(equipment);
        if (!inserted) {
            return EquipmentResult.failure("Equipment could not be saved. Please try again.");
        }

        EquipmentDTO saved = equipmentDao.findByAssetTag(equipment.getAssetTag());
        return EquipmentResult.success(saved);
    }

    /**
     * Retrieves a single piece of equipment by asset tag.
     *
     * @param assetTag the asset tag to look up
     * @return the matching equipment, or {@code null} if none found
     * @throws SQLException if a database error occurs
     */
    public EquipmentDTO getEquipment(String assetTag) throws SQLException {
        return equipmentDao.findByAssetTag(assetTag);
    }

    /**
     * Retrieves every equipment record, active or not. Used by admin/
     * Shop-Tech views that need the full inventory rather than just what
     * members are allowed to book.
     *
     * @return a list of all equipment, possibly empty
     * @throws SQLException if a database error occurs
     */
    public List<EquipmentDTO> getAllEquipment() throws SQLException {
        return equipmentDao.findAll();
    }

    /**
     * Retrieves only equipment currently {@link EquipmentStatus#AVAILABLE}
     * — the list a member sees when trying to book something (FR-03's
     * Equipment Status Board).
     *
     * @return a list of available equipment, possibly empty
     * @throws SQLException if a database error occurs
     */
    public List<EquipmentDTO> getAvailableEquipment() throws SQLException {
        return equipmentDao.findByStatus(EquipmentStatus.AVAILABLE);
    }

    /**
     * Retrieves equipment filtered by status.
     *
     * @param status the status to filter by
     * @return a list of matching equipment, possibly empty
     * @throws SQLException if a database error occurs
     */
    public List<EquipmentDTO> getEquipmentByStatus(EquipmentStatus status) throws SQLException {
        return equipmentDao.findByStatus(status);
    }

    /**
     * Retrieves equipment filtered by category.
     *
     * @param category the category to filter by
     * @return a list of matching equipment, possibly empty
     * @throws SQLException if a database error occurs
     */
    public List<EquipmentDTO> getEquipmentByCategory(EquipmentCategory category) throws SQLException {
        return equipmentDao.findByCategory(category);
    }

    /**
     * Validates and applies an update to an existing equipment record's
     * descriptive fields (make, model, category, name, status, credit
     * rate, location).
     *
     * @param equipment the equipment record carrying the new values,
     *                   identified by its {@code assetTag}
     * @return a success result carrying the updated equipment, or a
     *         failure result carrying a validation message
     * @throws SQLException if a database error occurs
     */
    public EquipmentResult updateEquipment(EquipmentDTO equipment) throws SQLException {
        if (equipment.getAssetTag() == null || equipment.getAssetTag().isBlank()) {
            return EquipmentResult.failure("Asset tag is required.");
        }
        if (equipmentDao.findByAssetTag(equipment.getAssetTag()) == null) {
            return EquipmentResult.failure(
                    "No equipment found with asset tag " + equipment.getAssetTag() + ".");
        }
        String validationError = validateDescriptiveFields(equipment);
        if (validationError != null) {
            return EquipmentResult.failure(validationError);
        }
        if (equipment.getStatus() == null) {
            return EquipmentResult.failure("Status is required.");
        }

        boolean updated = equipmentDao.update(equipment);
        if (!updated) {
            return EquipmentResult.failure("Equipment could not be updated. Please try again.");
        }
        return EquipmentResult.success(equipmentDao.findByAssetTag(equipment.getAssetTag()));
    }

    /**
     * Changes only the status of a piece of equipment — e.g. flipping to
     * {@code IN_USE} on check-in, {@code AVAILABLE} on check-out, or
     * {@code MAINTENANCE} when a Shop-Tech takes it down for service.
     *
     * @param assetTag  the asset tag to update
     * @param newStatus the new status
     * @return {@code true} if the update affected a row, {@code false} if
     *         no equipment with that asset tag exists
     * @throws SQLException if a database error occurs
     * @throws IllegalArgumentException if {@code assetTag} is blank or
     *                                   {@code newStatus} is {@code null}
     */
    public boolean updateEquipmentStatus(String assetTag, EquipmentStatus newStatus) throws SQLException {
        if (assetTag == null || assetTag.isBlank()) {
            throw new IllegalArgumentException("assetTag is required.");
        }
        if (newStatus == null) {
            throw new IllegalArgumentException("newStatus is required.");
        }
        return equipmentDao.updateStatus(assetTag, newStatus);
    }

    /**
     * Adds usage hours to a piece of equipment — called at the end of a
     * usage session (FR-03), and the input the wear-tracking side of
     * FR-05's predictive maintenance eventually reads from.
     *
     * @param assetTag the asset tag to update
     * @param hours    the number of hours to add, must be positive
     * @return {@code true} if the update affected a row, {@code false} if
     *         no equipment with that asset tag exists
     * @throws SQLException if a database error occurs
     * @throws IllegalArgumentException if {@code hours} is {@code null} or
     *                                   not positive
     */
    public boolean recordUsageHours(String assetTag, BigDecimal hours) throws SQLException {
        if (assetTag == null || assetTag.isBlank()) {
            throw new IllegalArgumentException("assetTag is required.");
        }
        if (hours == null || hours.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("hours must be a positive value.");
        }
        return equipmentDao.incrementUsageHours(assetTag, hours);
    }

    /**
     * Retires a piece of equipment (soft-delete): sets {@code active} to
     * {@code false} and {@code status} to {@code UNAVAILABLE} rather than
     * removing the row, since bookings/usage sessions/maintenance history
     * still reference it under {@code ON DELETE RESTRICT}.
     *
     * @param assetTag the asset tag to retire
     * @return {@code true} if the update affected a row, {@code false} if
     *         no equipment with that asset tag exists
     * @throws SQLException if a database error occurs
     */
    public boolean retireEquipment(String assetTag) throws SQLException {
        if (assetTag == null || assetTag.isBlank()) {
            throw new IllegalArgumentException("assetTag is required.");
        }
        return equipmentDao.deactivate(assetTag);
    }

    /**
     * Validates every field required to register brand-new equipment.
     *
     * @param equipment the equipment to validate
     * @return a human-readable error message, or {@code null} if valid
     */
    private String validateForRegistration(EquipmentDTO equipment) {
        if (equipment == null) {
            return "Equipment data is required.";
        }
        if (equipment.getAssetTag() == null || equipment.getAssetTag().isBlank()) {
            return "Asset tag is required.";
        }
        if (equipment.getRegisteredBy() <= 0) {
            return "registeredBy must be a valid user id.";
        }
        return validateDescriptiveFields(equipment);
    }

    /**
     * Validates the fields shared by both registration and update:
     * make, model, category, equipment name, and access credit rate.
     *
     * @param equipment the equipment to validate
     * @return a human-readable error message, or {@code null} if valid
     */
    private String validateDescriptiveFields(EquipmentDTO equipment) {
        if (equipment.getMake() == null || equipment.getMake().isBlank()) {
            return "Make is required.";
        }
        if (equipment.getModel() == null || equipment.getModel().isBlank()) {
            return "Model is required.";
        }
        if (equipment.getCategory() == null) {
            return "Category is required.";
        }
        if (equipment.getEquipmentName() == null || equipment.getEquipmentName().isBlank()) {
            return "Equipment name is required.";
        }
        if (equipment.getAccessCreditRate() == null) {
            return "Access credit rate is required.";
        }
        if (equipment.getAccessCreditRate().compareTo(BigDecimal.ZERO) < 0) {
            return "Access credit rate cannot be negative.";
        }
        return null;
    }
}
