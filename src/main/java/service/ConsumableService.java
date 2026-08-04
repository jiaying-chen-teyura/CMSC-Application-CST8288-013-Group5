package service;

import dao.ConsumableDao;
import dao.ConsumableDaoImpl;
import dto.ConsumableDTO;
import dto.InventoryReportDTO;
import dto.InventoryTransactionDTO;
import dto.InventoryTransactionType;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Business layer for the Consumable module (FR-04 Monitoring Consumables
 * &amp; Inventory). Named to match the "Consumable Service" box in the HLD's
 * Architecture Diagram (Section 5.2, Business Layer).
 * <p>
 * Enforces validation rules the DAO/database cannot or should not enforce
 * on their own, then delegates persistence to {@link ConsumableDao}.
 * Servlets call only this class, never {@link dao.ConsumableDaoImpl}
 * directly — that boundary is what makes this a genuine layered architecture.
 * <p>
 * Stock changes are always a two-step atomic-at-service-level operation:
 * <ol>
 *   <li>Update {@code consumables.current_stock} via the DAO.</li>
 *   <li>Append an {@code inventory_transactions} log entry via the DAO.</li>
 * </ol>
 * Both steps use separate connections from {@link util.DataSource}, which
 * is acceptable for this course scope (a full production solution would
 * wrap both in a single database transaction).
 *
 * @author Le Bao Thach Nguyen
 * @version 1.0
 */
public class ConsumableService {

    private final ConsumableDao consumableDao;

    /** Default constructor — wires the real JDBC DAO implementation. */
    public ConsumableService() {
        this.consumableDao = new ConsumableDaoImpl();
    }

    /**
     * Constructor for dependency injection (e.g. a test supplying a mock
     * {@link ConsumableDao} instead of {@link ConsumableDaoImpl}).
     *
     * @param consumableDao the DAO implementation to use
     */
    public ConsumableService(ConsumableDao consumableDao) {
        this.consumableDao = consumableDao;
    }

    // -------------------------------------------------------------------------
    // Consumable registration and management
    // -------------------------------------------------------------------------

    /**
     * Validates and registers a new consumable material in the system (FR-04).
     * <p>
     * Rules enforced:
     * <ul>
     *   <li>{@code materialName} is required and must be unique.</li>
     *   <li>{@code unit} is required.</li>
     *   <li>{@code restockLevel} is required and must be &gt;= 0.</li>
     *   <li>{@code unitDebitRate} is required and must be &gt;= 0.</li>
     * </ul>
     * On success, {@code currentStock} defaults to {@code 0} and
     * {@code active} defaults to {@code true}.
     *
     * @param consumable the consumable to register
     * @return a success result carrying the persisted consumable, or a
     *         failure result carrying a validation message
     * @throws SQLException if a database error occurs
     */
    public ConsumableResult registerConsumable(ConsumableDTO consumable) throws SQLException {
        String error = validateForRegistration(consumable);
        if (error != null) {
            return ConsumableResult.failure(error);
        }

        if (consumableDao.findByMaterialName(consumable.getMaterialName()) != null) {
            return ConsumableResult.failure(
                    "A consumable named '" + consumable.getMaterialName() + "' already exists.");
        }

        consumable.setCurrentStock(BigDecimal.ZERO);
        consumable.setActive(true);

        boolean inserted = consumableDao.insert(consumable);
        if (!inserted) {
            return ConsumableResult.failure("Consumable could not be saved. Please try again.");
        }

        ConsumableDTO saved = consumableDao.findById(consumable.getConsumableId());
        return ConsumableResult.success(saved);
    }

    /**
     * Retrieves a single consumable by its primary key.
     *
     * @param consumableId the primary key to look up
     * @return the matching consumable, or {@code null} if none found
     * @throws SQLException if a database error occurs
     */
    public ConsumableDTO getConsumable(int consumableId) throws SQLException {
        return consumableDao.findById(consumableId);
    }

    /**
     * Retrieves all consumables (active and retired).
     *
     * @return a list of all consumables, possibly empty
     * @throws SQLException if a database error occurs
     */
    public List<ConsumableDTO> getAllConsumables() throws SQLException {
        return consumableDao.findAll();
    }

    /**
     * Retrieves only active consumables — the list a member or Shop-Tech
     * sees when selecting materials during a session (FR-03, FR-04).
     *
     * @return a list of active consumables, possibly empty
     * @throws SQLException if a database error occurs
     */
    public List<ConsumableDTO> getActiveConsumables() throws SQLException {
        return consumableDao.findAllActive();
    }

    /**
     * Retrieves consumables that require restocking (current stock &lt;=
     * restock level), used to drive LOW_STOCK alerts (FR-04).
     *
     * @return a list of low-stock consumables, possibly empty
     * @throws SQLException if a database error occurs
     */
    public List<ConsumableDTO> getLowStockConsumables() throws SQLException {
        return consumableDao.findBelowRestockLevel();
    }

    /**
     * Validates and applies an update to an existing consumable's descriptive
     * fields ({@code materialName}, {@code unit}, {@code restockLevel},
     * {@code unitDebitRate}). Does not touch {@code currentStock} or
     * {@code active}.
     *
     * @param consumable the consumable carrying the new values
     * @return a success result carrying the updated consumable, or a failure
     *         result with a validation message
     * @throws SQLException if a database error occurs
     */
    public ConsumableResult updateConsumable(ConsumableDTO consumable) throws SQLException {
        if (consumable.getConsumableId() <= 0) {
            return ConsumableResult.failure("Consumable ID is required.");
        }
        if (consumableDao.findById(consumable.getConsumableId()) == null) {
            return ConsumableResult.failure(
                    "No consumable found with ID " + consumable.getConsumableId() + ".");
        }

        String error = validateDescriptiveFields(consumable);
        if (error != null) {
            return ConsumableResult.failure(error);
        }

        boolean updated = consumableDao.update(consumable);
        if (!updated) {
            return ConsumableResult.failure("Consumable could not be updated. Please try again.");
        }
        return ConsumableResult.success(consumableDao.findById(consumable.getConsumableId()));
    }

    /**
     * Retires a consumable (soft-delete). A hard delete is not offered because
     * {@code material_usage} and {@code inventory_transactions} reference this
     * record under {@code ON DELETE RESTRICT}.
     *
     * @param consumableId the primary key to retire
     * @return {@code true} if the update affected a row
     * @throws SQLException if a database error occurs
     * @throws IllegalArgumentException if {@code consumableId} is &lt;= 0
     */
    public boolean retireConsumable(int consumableId) throws SQLException {
        if (consumableId <= 0) {
            throw new IllegalArgumentException("consumableId must be a positive value.");
        }
        return consumableDao.deactivate(consumableId);
    }

    // -------------------------------------------------------------------------
    // Stock changes (FR-04 / donation contribution / session usage)
    // -------------------------------------------------------------------------

    /**
     * Records a stock addition (DONATION contribution type — member donates
     * material, earning credits elsewhere) or a formal RESTOCK by a Shop-Tech.
     * <p>
     * This method updates {@code consumables.current_stock} and appends an
     * {@code inventory_transactions} log entry.
     *
     * @param consumableId    the consumable to restock
     * @param quantity        the amount to add (must be positive)
     * @param transactionType must be {@link InventoryTransactionType#RESTOCK}
     *                        or {@link InventoryTransactionType#DONATION}
     * @param performedBy     user_id of the actor
     * @param notes           optional note (may be {@code null})
     * @return {@code true} if both the stock update and log insert succeeded
     * @throws SQLException             if a database error occurs
     * @throws IllegalArgumentException if arguments fail precondition checks
     */
    public boolean addStock(int consumableId, BigDecimal quantity,
            InventoryTransactionType transactionType,
            int performedBy, String notes) throws SQLException {

        validateStockArgs(consumableId, quantity, performedBy);
        if (transactionType != InventoryTransactionType.RESTOCK
                && transactionType != InventoryTransactionType.DONATION) {
            throw new IllegalArgumentException(
                    "addStock only accepts RESTOCK or DONATION transaction types.");
        }
        if (consumableDao.findById(consumableId) == null) {
            throw new IllegalArgumentException(
                    "No active consumable found with ID " + consumableId + ".");
        }

        boolean stockUpdated = consumableDao.incrementStock(consumableId, quantity);
        if (!stockUpdated) {
            return false;
        }

        InventoryTransactionDTO tx = new InventoryTransactionDTO(
                consumableId, transactionType, quantity, performedBy, notes);
        consumableDao.insertTransaction(tx);
        return true;
    }

    /**
     * Records stock consumption during an equipment usage session (FR-03
     * debit side). Updates {@code consumables.current_stock} (decrement) and
     * appends a USAGE {@code inventory_transactions} log entry.
     * <p>
     * Returns {@code false} — rather than throwing — if there is insufficient
     * stock, so the calling servlet can report a clean user-facing message
     * instead of an unhandled exception.
     *
     * @param consumableId the consumable to consume from
     * @param quantity     the amount consumed (must be positive)
     * @param performedBy  user_id of the member consuming the material
     * @param notes        optional note (may be {@code null})
     * @return {@code true} if both the stock update and log insert succeeded;
     *         {@code false} if stock was insufficient
     * @throws SQLException             if a database error occurs
     * @throws IllegalArgumentException if arguments fail precondition checks
     */
    public boolean consumeStock(int consumableId, BigDecimal quantity,
            int performedBy, String notes) throws SQLException {

        validateStockArgs(consumableId, quantity, performedBy);

        ConsumableDTO existing = consumableDao.findById(consumableId);
        if (existing == null) {
            throw new IllegalArgumentException(
                    "No active consumable found with ID " + consumableId + ".");
        }
        if (existing.getCurrentStock().compareTo(quantity) < 0) {
            // Insufficient stock — caller displays a user-friendly message
            return false;
        }

        boolean stockUpdated = consumableDao.decrementStock(consumableId, quantity);
        if (!stockUpdated) {
            return false;
        }

        InventoryTransactionDTO tx = new InventoryTransactionDTO(
                consumableId, InventoryTransactionType.USAGE, quantity.negate(),
                performedBy, notes);
        consumableDao.insertTransaction(tx);
        return true;
    }

    // -------------------------------------------------------------------------
    // Reporting (FR-04, FR-06)
    // -------------------------------------------------------------------------

    /**
     * Returns the full inventory report — one row per active consumable —
     * including current stock, average daily consumption over the last 30 days,
     * and projected days until depletion. Sourced from the
     * {@code v_consumable_inventory_report} view (FR-04).
     *
     * @return a list of inventory report rows, possibly empty
     * @throws SQLException if a database error occurs
     */
    public List<InventoryReportDTO> getInventoryReport() throws SQLException {
        return consumableDao.getInventoryReport();
    }

    /**
     * Returns the full transaction history for a given consumable (FR-06
     * audit trail).
     *
     * @param consumableId the consumable whose history to fetch
     * @return a list of transactions ordered most-recent-first, possibly empty
     * @throws SQLException if a database error occurs
     */
    public List<InventoryTransactionDTO> getTransactionHistory(int consumableId)
            throws SQLException {
        if (consumableId <= 0) {
            throw new IllegalArgumentException("consumableId must be a positive value.");
        }
        return consumableDao.findTransactionsByConsumable(consumableId);
    }

    // -------------------------------------------------------------------------
    // Private validation helpers
    // -------------------------------------------------------------------------

    /**
     * Validates every field required to register a brand-new consumable.
     *
     * @param consumable the consumable to validate
     * @return a human-readable error message, or {@code null} if valid
     */
    private String validateForRegistration(ConsumableDTO consumable) {
        if (consumable == null) {
            return "Consumable data is required.";
        }
        return validateDescriptiveFields(consumable);
    }

    /**
     * Validates the fields shared by both registration and update:
     * materialName, unit, restockLevel, unitDebitRate.
     *
     * @param consumable the consumable to validate
     * @return a human-readable error message, or {@code null} if valid
     */
    private String validateDescriptiveFields(ConsumableDTO consumable) {
        if (consumable.getMaterialName() == null || consumable.getMaterialName().isBlank()) {
            return "Material name is required.";
        }
        if (consumable.getUnit() == null) {
            return "Unit is required.";
        }
        if (consumable.getRestockLevel() == null) {
            return "Restock level is required.";
        }
        if (consumable.getRestockLevel().compareTo(BigDecimal.ZERO) < 0) {
            return "Restock level cannot be negative.";
        }
        if (consumable.getUnitDebitRate() == null) {
            return "Unit debit rate is required.";
        }
        if (consumable.getUnitDebitRate().compareTo(BigDecimal.ZERO) < 0) {
            return "Unit debit rate cannot be negative.";
        }
        return null;
    }

    /**
     * Shared precondition check for {@link #addStock} and
     * {@link #consumeStock}.
     *
     * @throws IllegalArgumentException if any argument fails its check
     */
    private void validateStockArgs(int consumableId, BigDecimal quantity, int performedBy) {
        if (consumableId <= 0) {
            throw new IllegalArgumentException("consumableId must be a positive value.");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("quantity must be a positive value.");
        }
        if (performedBy <= 0) {
            throw new IllegalArgumentException("performedBy must be a valid user id.");
        }
    }
}
