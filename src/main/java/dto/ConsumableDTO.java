package dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Data Transfer Object representing a single row of the {@code consumables}
 * table (see {@code src/main/resources/Database/CMSC_database.sql}).
 * <p>
 * Carries data only — no business logic, no persistence awareness, no
 * validation. Field-for-field mapping to the real schema:
 * <pre>
 * consumable_id     INT AUTO_INCREMENT PK  -> consumableId
 * material_name     VARCHAR(100) NOT NULL  -> materialName
 * unit              ENUM(...)    NOT NULL  -> unit
 * current_stock     DECIMAL(12,2)          -> currentStock
 * restock_level     DECIMAL(12,2)          -> restockLevel
 * unit_debit_rate   DECIMAL(10,2)          -> unitDebitRate
 * active            BOOLEAN                -> active
 * </pre>
 * {@code currentStock}, {@code restockLevel}, and {@code unitDebitRate}
 * are {@link BigDecimal} rather than {@code double} on purpose — the HLD's
 * Data Integrity section calls out that "financial values use the DECIMAL
 * data type to avoid calculation errors," and {@code double} would
 * reintroduce exactly the floating-point rounding risk that column
 * choice is meant to avoid.
 *
 * @author Le Bao Thach Nguyen
 * @version 1.0
 */
public class ConsumableDTO {

    private int consumableId;
    private String materialName;
    private ConsumableUnit unit;
    private BigDecimal currentStock;
    private BigDecimal restockLevel;
    private BigDecimal unitDebitRate;
    private boolean active;

    /** No-arg constructor, required for JavaBean-style construction. */
    public ConsumableDTO() {
    }

    /**
     * Constructs a {@code ConsumableDTO} from all persisted fields.
     * Used by the DAO when mapping a result set row back to an object.
     *
     * @param consumableId  auto-generated primary key
     * @param materialName  unique display name (e.g. {@code "PLA Filament – White"})
     * @param unit          measurement unit
     * @param currentStock  quantity currently in stock
     * @param restockLevel  threshold below which a LOW_STOCK notification fires
     * @param unitDebitRate debit amount charged per unit consumed by a member
     * @param active        {@code false} means this consumable has been retired
     */
    public ConsumableDTO(int consumableId, String materialName, ConsumableUnit unit,
            BigDecimal currentStock, BigDecimal restockLevel,
            BigDecimal unitDebitRate, boolean active) {
        this.consumableId = consumableId;
        this.materialName = materialName;
        this.unit = unit;
        this.currentStock = currentStock;
        this.restockLevel = restockLevel;
        this.unitDebitRate = unitDebitRate;
        this.active = active;
    }

    /**
     * Convenience constructor for creating a brand-new consumable before
     * insertion (no ID yet — the database will assign it via AUTO_INCREMENT).
     * {@code currentStock} defaults to {@code 0} and {@code active} to
     * {@code true}.
     *
     * @param materialName  unique display name of the material
     * @param unit          measurement unit
     * @param restockLevel  restock alert threshold
     * @param unitDebitRate debit rate per unit consumed
     */
    public ConsumableDTO(String materialName, ConsumableUnit unit,
            BigDecimal restockLevel, BigDecimal unitDebitRate) {
        this.materialName = materialName;
        this.unit = unit;
        this.currentStock = BigDecimal.ZERO;
        this.restockLevel = restockLevel;
        this.unitDebitRate = unitDebitRate;
        this.active = true;
    }

    /** @return the auto-generated primary key (0 if not yet persisted) */
    public int getConsumableId() {
        return consumableId;
    }

    /** @param consumableId the primary key to set (called by DAO after insert) */
    public void setConsumableId(int consumableId) {
        this.consumableId = consumableId;
    }

    /** @return the unique display name of this material */
    public String getMaterialName() {
        return materialName;
    }

    /** @param materialName the display name to set */
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    /** @return the measurement unit */
    public ConsumableUnit getUnit() {
        return unit;
    }

    /** @param unit the measurement unit to set */
    public void setUnit(ConsumableUnit unit) {
        this.unit = unit;
    }

    /** @return the current quantity in stock */
    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    /** @param currentStock the current stock level to set */
    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock;
    }

    /** @return the restock alert threshold */
    public BigDecimal getRestockLevel() {
        return restockLevel;
    }

    /** @param restockLevel the restock threshold to set */
    public void setRestockLevel(BigDecimal restockLevel) {
        this.restockLevel = restockLevel;
    }

    /** @return the debit rate charged per unit consumed */
    public BigDecimal getUnitDebitRate() {
        return unitDebitRate;
    }

    /** @param unitDebitRate the debit rate to set */
    public void setUnitDebitRate(BigDecimal unitDebitRate) {
        this.unitDebitRate = unitDebitRate;
    }

    /** @return {@code true} if this consumable is active (not soft-deleted) */
    public boolean isActive() {
        return active;
    }

    /** @param active the active flag to set */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Two {@code ConsumableDTO}s are equal if they share the same
     * {@code consumableId}. If neither has been persisted yet (id = 0),
     * falls back to comparing {@code materialName}, since
     * {@code material_name} carries a unique constraint on the table.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsumableDTO)) {
            return false;
        }
        ConsumableDTO that = (ConsumableDTO) o;
        if (this.consumableId != 0 && that.consumableId != 0) {
            return this.consumableId == that.consumableId;
        }
        return Objects.equals(this.materialName, that.materialName);
    }

    @Override
    public int hashCode() {
        return consumableId != 0 ? Integer.hashCode(consumableId)
                                 : Objects.hash(materialName);
    }

    @Override
    public String toString() {
        return "ConsumableDTO{"
                + "consumableId=" + consumableId
                + ", materialName='" + materialName + '\''
                + ", unit=" + unit
                + ", currentStock=" + currentStock
                + ", restockLevel=" + restockLevel
                + ", unitDebitRate=" + unitDebitRate
                + ", active=" + active
                + '}';
    }
}
