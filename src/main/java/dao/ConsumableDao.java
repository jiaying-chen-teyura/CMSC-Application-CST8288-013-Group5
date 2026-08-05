package dao;

import dto.ConsumableDTO;
import dto.InventoryTransactionDTO;
import dto.InventoryReportDTO;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Defines persistence operations for the Consumable module, backed by the
 * {@code consumables}, {@code inventory_transactions}, and
 * {@code v_consumable_inventory_report} view in {@code CMSC_database.sql}.
 * <p>
 * Implementations must not contain business logic — validation and rules
 * belong in {@link service.ConsumableService}, which programs against this
 * interface rather than {@link ConsumableDaoImpl} directly, so the JDBC
 * implementation is swappable (e.g. for a mock in a unit test) without any
 * change to the business layer.
 * <p>
 * Design note (HLD Section 7, DAO Pattern): No XxxDao ever calls another
 * XxxDao directly. Cross-module interactions (e.g., crediting a member after
 * a donation) are handled at the Service layer, not here.
 *
 * @author Le Bao Thach Nguyen
 * @version 1.0
 */
public interface ConsumableDao {

    // -------------------------------------------------------------------------
    // Consumable CRUD
    // -------------------------------------------------------------------------

    /**
     * Retrieves a single consumable record by its primary key.
     *
     * @param consumableId the primary key to look up
     * @return the matching consumable, or {@code null} if none found
     * @throws SQLException if the query fails
     */
    ConsumableDTO findById(int consumableId) throws SQLException;

    /**
     * Retrieves a consumable by its unique material name.
     *
     * @param materialName the material name to look up
     * @return the matching consumable, or {@code null} if none found
     * @throws SQLException if the query fails
     */
    ConsumableDTO findByMaterialName(String materialName) throws SQLException;

    /**
     * Retrieves every consumable record, active or not.
     *
     * @return a list of all consumables, possibly empty
     * @throws SQLException if the query fails
     */
    List<ConsumableDTO> findAll() throws SQLException;

    /**
     * Retrieves only active consumables (soft-delete filter).
     *
     * @return a list of active consumables, possibly empty
     * @throws SQLException if the query fails
     */
    List<ConsumableDTO> findAllActive() throws SQLException;

    /**
     * Retrieves consumables whose current stock is at or below their
     * restock level — used to trigger LOW_STOCK notifications (FR-04).
     *
     * @return a list of low-stock consumables, possibly empty
     * @throws SQLException if the query fails
     */
    List<ConsumableDTO> findBelowRestockLevel() throws SQLException;

    /**
     * Inserts a new consumable record. The generated primary key is set on
     * the passed DTO after a successful insert.
     *
     * @param consumable the consumable to insert ({@code consumableId} is ignored)
     * @return {@code true} if exactly one row was inserted
     * @throws SQLException if the insert fails (including a duplicate
     *                       {@code material_name} unique-constraint violation)
     */
    boolean insert(ConsumableDTO consumable) throws SQLException;

    /**
     * Updates the mutable fields of an existing consumable record
     * ({@code material_name}, {@code unit}, {@code restock_level},
     * {@code unit_debit_rate}). Does not touch {@code current_stock} or
     * {@code active} — those are updated through their own dedicated methods.
     *
     * @param consumable the consumable carrying the new values, identified
     *                   by its {@code consumableId}
     * @return {@code true} if exactly one row was updated
     * @throws SQLException if the update fails
     */
    boolean update(ConsumableDTO consumable) throws SQLException;

    /**
     * Increments the current stock of a consumable — called when a member
     * donates material (FR-04 / contribution credit flow) or when a Shop-Tech
     * restocks inventory.
     *
     * @param consumableId  the consumable to update
     * @param quantityToAdd the amount to add (must be positive)
     * @return {@code true} if exactly one row was updated
     * @throws SQLException if the update fails
     */
    boolean incrementStock(int consumableId, BigDecimal quantityToAdd) throws SQLException;

    /**
     * Decrements the current stock of a consumable — called when a member
     * consumes material during an equipment usage session (FR-03 / FR-04).
     * The business layer must verify that sufficient stock exists before
     * calling this; the DB CHECK constraint prevents the value from going
     * negative, but a clear business-layer message is better than a raw
     * {@link SQLException}.
     *
     * @param consumableId     the consumable to update
     * @param quantityToRemove the amount to subtract (must be positive)
     * @return {@code true} if exactly one row was updated
     * @throws SQLException if the update fails or stock would go below zero
     */
    boolean decrementStock(int consumableId, BigDecimal quantityToRemove) throws SQLException;

    /**
     * Soft-deletes a consumable by setting {@code active = FALSE}.
     * A hard DELETE is not offered because {@code consumables} has child rows
     * under {@code ON DELETE RESTRICT} ({@code material_usage},
     * {@code inventory_transactions}).
     *
     * @param consumableId the primary key to deactivate
     * @return {@code true} if exactly one row was updated
     * @throws SQLException if the update fails
     */
    boolean deactivate(int consumableId) throws SQLException;

    // -------------------------------------------------------------------------
    // Inventory transactions
    // -------------------------------------------------------------------------

    /**
     * Appends an inventory transaction log entry to
     * {@code inventory_transactions}. Called any time stock changes (donation,
     * restock, usage, adjustment) to maintain a full audit trail.
     * The generated primary key is set on the passed DTO after a successful insert.
     *
     * @param transaction the transaction to record
     * @return {@code true} if exactly one row was inserted
     * @throws SQLException if the insert fails
     */
    boolean insertTransaction(InventoryTransactionDTO transaction) throws SQLException;

    /**
     * Retrieves the full transaction history for a given consumable, ordered
     * by {@code transaction_time} descending (most recent first).
     *
     * @param consumableId the consumable whose history to fetch
     * @return a list of transactions, possibly empty
     * @throws SQLException if the query fails
     */
    List<InventoryTransactionDTO> findTransactionsByConsumable(int consumableId)
            throws SQLException;

    // -------------------------------------------------------------------------
    // Reporting (FR-04, FR-06)
    // -------------------------------------------------------------------------

    /**
     * Assembles inventory report rows from the
     * {@code v_consumable_inventory_report} view, which pre-computes
     * average daily consumption and projected days until depletion.
     * Returns one {@link InventoryReportDTO} per active consumable.
     *
     * @return a list of inventory report rows, possibly empty
     * @throws SQLException if the query fails
     */
    List<InventoryReportDTO> getInventoryReport() throws SQLException;
}
