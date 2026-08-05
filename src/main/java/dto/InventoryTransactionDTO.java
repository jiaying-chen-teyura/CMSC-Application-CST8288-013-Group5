package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object representing a single row of the
 * {@code inventory_transactions} table — the append-only audit log of every
 * stock change (see {@code src/main/resources/Database/CMSC_database.sql}).
 * <p>
 * Carries data only — no business logic, no persistence awareness, no
 * validation. Field-for-field mapping to the real schema:
 * <pre>
 * inventory_transaction_id  INT AUTO_INCREMENT PK  -> inventoryTransactionId
 * consumable_id             INT NOT NULL           -> consumableId
 * transaction_type          ENUM(...)              -> transactionType
 * quantity_change           DECIMAL(12,2)          -> quantityChange
 * performed_by              INT NOT NULL           -> performedBy
 * transaction_time          DATETIME               -> transactionTime
 * notes                     VARCHAR(255) NULL      -> notes
 * </pre>
 * {@code quantityChange} is positive for stock additions (RESTOCK, DONATION)
 * and negative for deductions (USAGE). The database enforces
 * {@code quantity_change <> 0} via a CHECK constraint.
 *
 * @author Le Bao Thach Nguyen
 * @version 1.0
 */
public class InventoryTransactionDTO {

    private int inventoryTransactionId;
    private int consumableId;
    private InventoryTransactionType transactionType;
    private BigDecimal quantityChange;
    private int performedBy;
    private LocalDateTime transactionTime;
    private String notes;

    /** No-arg constructor, required for JavaBean-style construction. */
    public InventoryTransactionDTO() {
    }

    /**
     * Constructs an {@code InventoryTransactionDTO} from all persisted fields.
     * Used by the DAO when mapping a result set row back to an object.
     *
     * @param inventoryTransactionId auto-generated primary key
     * @param consumableId           the consumable this transaction affects
     * @param transactionType        type of stock change
     * @param quantityChange         amount added (positive) or removed (negative)
     * @param performedBy            user_id of the actor who performed the action
     * @param transactionTime        timestamp recorded by the database
     * @param notes                  optional free-text note, may be {@code null}
     */
    public InventoryTransactionDTO(int inventoryTransactionId, int consumableId,
            InventoryTransactionType transactionType, BigDecimal quantityChange,
            int performedBy, LocalDateTime transactionTime, String notes) {
        this.inventoryTransactionId = inventoryTransactionId;
        this.consumableId = consumableId;
        this.transactionType = transactionType;
        this.quantityChange = quantityChange;
        this.performedBy = performedBy;
        this.transactionTime = transactionTime;
        this.notes = notes;
    }

    /**
     * Convenience constructor for creating a new transaction before insertion
     * (no ID or timestamp yet — both are assigned by the database on insert).
     *
     * @param consumableId    the consumable this transaction affects
     * @param transactionType type of stock change
     * @param quantityChange  amount added or removed (non-zero)
     * @param performedBy     user_id of the actor
     * @param notes           optional note, may be {@code null}
     */
    public InventoryTransactionDTO(int consumableId,
            InventoryTransactionType transactionType, BigDecimal quantityChange,
            int performedBy, String notes) {
        this.consumableId = consumableId;
        this.transactionType = transactionType;
        this.quantityChange = quantityChange;
        this.performedBy = performedBy;
        this.notes = notes;
    }

    /** @return the auto-generated primary key (0 if not yet persisted) */
    public int getInventoryTransactionId() {
        return inventoryTransactionId;
    }

    /** @param inventoryTransactionId the primary key to set */
    public void setInventoryTransactionId(int inventoryTransactionId) {
        this.inventoryTransactionId = inventoryTransactionId;
    }

    /** @return the consumable_id this transaction applies to */
    public int getConsumableId() {
        return consumableId;
    }

    /** @param consumableId the consumable_id to set */
    public void setConsumableId(int consumableId) {
        this.consumableId = consumableId;
    }

    /** @return the type of stock change */
    public InventoryTransactionType getTransactionType() {
        return transactionType;
    }

    /** @param transactionType the transaction type to set */
    public void setTransactionType(InventoryTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    /** @return the quantity added (positive) or removed (negative) */
    public BigDecimal getQuantityChange() {
        return quantityChange;
    }

    /** @param quantityChange the quantity change to set (must be non-zero) */
    public void setQuantityChange(BigDecimal quantityChange) {
        this.quantityChange = quantityChange;
    }

    /** @return the user_id of the actor who performed this transaction */
    public int getPerformedBy() {
        return performedBy;
    }

    /** @param performedBy the acting user's id to set */
    public void setPerformedBy(int performedBy) {
        this.performedBy = performedBy;
    }

    /** @return the timestamp of the transaction, or {@code null} if not yet persisted */
    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    /** @param transactionTime the transaction timestamp to set */
    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    /** @return the optional free-text note, or {@code null} */
    public String getNotes() {
        return notes;
    }

    /** @param notes the note to set, may be {@code null} */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryTransactionDTO)) {
            return false;
        }
        InventoryTransactionDTO that = (InventoryTransactionDTO) o;
        return this.inventoryTransactionId == that.inventoryTransactionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventoryTransactionId);
    }

    @Override
    public String toString() {
        return "InventoryTransactionDTO{"
                + "inventoryTransactionId=" + inventoryTransactionId
                + ", consumableId=" + consumableId
                + ", transactionType=" + transactionType
                + ", quantityChange=" + quantityChange
                + ", performedBy=" + performedBy
                + ", transactionTime=" + transactionTime
                + ", notes='" + notes + '\''
                + '}';
    }
}
