package dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Read-model DTO assembled by {@link service.ConsumableService} from the
 * {@code v_consumable_inventory_report} view (see
 * {@code src/main/resources/Database/CMSC_database.sql}).
 * <p>
 * This DTO is <em>never persisted directly</em>. It exists solely to satisfy
 * FR-04's requirement for a per-consumable inventory report that includes
 * current stock level, average daily consumption rate, and projected time
 * until depletion — without polluting the persistence-mapped
 * {@link ConsumableDTO} with computed fields that have no column in the
 * {@code consumables} table.
 *
 * @author Le Bao Thach Nguyen
 * @version 1.0
 */
public class InventoryReportDTO {

    private int consumableId;
    private String materialName;
    private ConsumableUnit unit;
    private BigDecimal currentStock;
    private BigDecimal restockLevel;

    /**
     * Average units consumed per day over the last 30 days.
     * {@code null} when there is no usage data to compute from.
     */
    private BigDecimal averageDailyConsumption;

    /**
     * Estimated days until current stock is depleted at the current
     * consumption rate. {@code null} when the consumption rate is zero
     * (stock would never deplete at that rate).
     */
    private BigDecimal projectedDaysUntilDepletion;

    /**
     * {@code true} when {@code current_stock <= restock_level} — maps to
     * the {@code stock_status} computed column in the view
     * ({@code 'RESTOCK_REQUIRED'} vs {@code 'SUFFICIENT'}).
     */
    private boolean restockRequired;

    /** No-arg constructor, required for JavaBean-style construction. */
    public InventoryReportDTO() {
    }

    /**
     * Constructs an {@code InventoryReportDTO} directly from the columns
     * returned by the {@code v_consumable_inventory_report} view.
     *
     * @param consumableId                the consumable's primary key
     * @param materialName                unique display name
     * @param unit                        measurement unit
     * @param currentStock                current quantity in stock
     * @param restockLevel                alert threshold
     * @param averageDailyConsumption     avg. daily usage over last 30 days,
     *                                    or {@code null} if no usage data
     * @param projectedDaysUntilDepletion days until stock hits zero at current
     *                                    rate, or {@code null} if rate is zero
     * @param restockRequired             {@code true} when stock &lt;= threshold
     */
    public InventoryReportDTO(int consumableId, String materialName, ConsumableUnit unit,
            BigDecimal currentStock, BigDecimal restockLevel,
            BigDecimal averageDailyConsumption, BigDecimal projectedDaysUntilDepletion,
            boolean restockRequired) {
        this.consumableId = consumableId;
        this.materialName = materialName;
        this.unit = unit;
        this.currentStock = currentStock;
        this.restockLevel = restockLevel;
        this.averageDailyConsumption = averageDailyConsumption;
        this.projectedDaysUntilDepletion = projectedDaysUntilDepletion;
        this.restockRequired = restockRequired;
    }

    /** @return the consumable's primary key */
    public int getConsumableId() {
        return consumableId;
    }

    /** @param consumableId the primary key to set */
    public void setConsumableId(int consumableId) {
        this.consumableId = consumableId;
    }

    /** @return the unique display name of the material */
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

    /** @param currentStock the current stock to set */
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

    /** @return average daily consumption over last 30 days, or {@code null} */
    public BigDecimal getAverageDailyConsumption() {
        return averageDailyConsumption;
    }

    /** @param averageDailyConsumption the average daily consumption to set */
    public void setAverageDailyConsumption(BigDecimal averageDailyConsumption) {
        this.averageDailyConsumption = averageDailyConsumption;
    }

    /** @return projected days until depletion at current rate, or {@code null} */
    public BigDecimal getProjectedDaysUntilDepletion() {
        return projectedDaysUntilDepletion;
    }

    /** @param projectedDaysUntilDepletion the projected days to set */
    public void setProjectedDaysUntilDepletion(BigDecimal projectedDaysUntilDepletion) {
        this.projectedDaysUntilDepletion = projectedDaysUntilDepletion;
    }

    /** @return {@code true} if current stock is at or below the restock threshold */
    public boolean isRestockRequired() {
        return restockRequired;
    }

    /** @param restockRequired the restock-required flag to set */
    public void setRestockRequired(boolean restockRequired) {
        this.restockRequired = restockRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryReportDTO)) {
            return false;
        }
        InventoryReportDTO that = (InventoryReportDTO) o;
        return this.consumableId == that.consumableId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(consumableId);
    }

    @Override
    public String toString() {
        return "InventoryReportDTO{"
                + "consumableId=" + consumableId
                + ", materialName='" + materialName + '\''
                + ", unit=" + unit
                + ", currentStock=" + currentStock
                + ", restockLevel=" + restockLevel
                + ", averageDailyConsumption=" + averageDailyConsumption
                + ", projectedDaysUntilDepletion=" + projectedDaysUntilDepletion
                + ", restockRequired=" + restockRequired
                + '}';
    }
}
