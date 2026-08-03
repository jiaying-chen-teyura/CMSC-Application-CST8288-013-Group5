package dao;

import dto.EquipmentCategory;
import dto.EquipmentDTO;
import dto.EquipmentStatus;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Defines persistence operations for {@link EquipmentDTO} records, backed
 * by the {@code equipment} table in {@code CMSC_database.sql}.
 * <p>
 * Implementations must not contain business logic — validation and rules
 * belong in {@link service.EquipmentService}, which programs against this
 * interface rather than {@link EquipmentDaoImpl} directly, so the JDBC
 * implementation is swappable (e.g. for a mock in a unit test) without any
 * change to the business layer.
 *
 * @author Oladimeji Durojaiye
 * @version 1.0
 */
public interface EquipmentDao {

    /**
     * Retrieves a single equipment record by its asset tag.
     *
     * @param assetTag the asset tag to look up
     * @return the matching equipment, or {@code null} if none found
     * @throws SQLException if the query fails
     */
    EquipmentDTO findByAssetTag(String assetTag) throws SQLException;

    /**
     * Retrieves every equipment record, active or not.
     *
     * @return a list of all equipment, possibly empty
     * @throws SQLException if the query fails
     */
    List<EquipmentDTO> findAll() throws SQLException;

    /**
     * Retrieves all equipment currently in the given status.
     *
     * @param status the status to filter by
     * @return a list of matching equipment, possibly empty
     * @throws SQLException if the query fails
     */
    List<EquipmentDTO> findByStatus(EquipmentStatus status) throws SQLException;

    /**
     * Retrieves all equipment in the given category.
     *
     * @param category the category to filter by
     * @return a list of matching equipment, possibly empty
     * @throws SQLException if the query fails
     */
    List<EquipmentDTO> findByCategory(EquipmentCategory category) throws SQLException;

    /**
     * Inserts a new equipment record. {@code status} defaults to
     * {@code AVAILABLE} and {@code total_usage_hours} to {@code 0.00} at
     * the database level if not supplied on the DTO.
     *
     * @param equipment the equipment to insert
     * @return {@code true} if exactly one row was inserted
     * @throws SQLException if the insert fails (including a duplicate
     *                       {@code asset_tag} primary-key violation)
     */
    boolean insert(EquipmentDTO equipment) throws SQLException;

    /**
     * Updates the mutable descriptive fields of an existing equipment
     * record ({@code make}, {@code model}, {@code category},
     * {@code equipment_name}, {@code status}, {@code access_credit_rate},
     * {@code location}). Does not touch {@code total_usage_hours},
     * {@code registered_by}/{@code registered_at}, or {@code active} —
     * those are updated through their own dedicated methods below.
     *
     * @param equipment the equipment record carrying the new values,
     *                   identified by its {@code assetTag}
     * @return {@code true} if exactly one row was updated
     * @throws SQLException if the update fails
     */
    boolean update(EquipmentDTO equipment) throws SQLException;

    /**
     * Updates only the status of an equipment record.
     *
     * @param assetTag  the asset tag to update
     * @param newStatus the new status
     * @return {@code true} if exactly one row was updated
     * @throws SQLException if the update fails
     */
    boolean updateStatus(String assetTag, EquipmentStatus newStatus) throws SQLException;

    /**
     * Adds to the equipment's cumulative usage hours (called at the end of
     * a usage session, per FR-03 / FR-05's wear-tracking need).
     *
     * @param assetTag     the asset tag to update
     * @param hoursToAdd   the number of hours to add (must be positive)
     * @return {@code true} if exactly one row was updated
     * @throws SQLException if the update fails
     */
    boolean incrementUsageHours(String assetTag, BigDecimal hoursToAdd) throws SQLException;

    /**
     * Soft-deletes an equipment record by setting {@code active = FALSE}
     * and {@code status = UNAVAILABLE}, rather than deleting the row.
     * <p>
     * A hard {@code DELETE} is not offered because {@code equipment} has
     * child rows under {@code ON DELETE RESTRICT} (bookings, usage
     * sessions, maintenance tasks) — the schema is deliberately built for
     * a soft-delete via the {@code active} flag instead.
     *
     * @param assetTag the asset tag to deactivate
     * @return {@code true} if exactly one row was updated
     * @throws SQLException if the update fails
     */
    boolean deactivate(String assetTag) throws SQLException;
}
