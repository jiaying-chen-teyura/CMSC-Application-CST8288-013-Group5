package transferobjects;

/**
 * Represents a consumable item and its inventory-related state.
 * @author Le Bao Thach Nguyen 
 */
public class ConsumableDTO {

    public enum Unit { GRAM, MILLILITRE, SHEET, PIECE }

    private Integer consumableId;
    private String materialName;
    private Unit unit;
    private double currentStock;
    private double restockLevel;
    private double unitDebitRate;
    private boolean active = true;

    // Read-only convenience fields populated from v_consumable_inventory_report (FR-04)
    private String stockStatus;
    private Double averageDailyConsumption;
    private Double projectedDaysUntilDepletion;

    /**
     * Returns the unique identifier of the consumable.
     *
     * @return the consumable identifier
     */
    public Integer getConsumableId() { return consumableId; }

    /**
     * Sets the unique identifier of the consumable.
     *
     * @param consumableId the consumable identifier to assign
     */
    public void setConsumableId(Integer consumableId) { this.consumableId = consumableId; }

    /**
     * Returns the material name.
     *
     * @return the material name
     */
    public String getMaterialName() { return materialName; }

    /**
     * Sets the material name.
     *
     * @param materialName the material name to assign
     */
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    /**
     * Returns the unit of measurement for this consumable.
     *
     * @return the unit of measurement
     */
    public Unit getUnit() { return unit; }

    /**
     * Sets the unit of measurement for this consumable.
     *
     * @param unit the unit of measurement to assign
     */
    public void setUnit(Unit unit) { this.unit = unit; }

    /**
     * Returns the current stock level.
     *
     * @return the current stock level
     */
    public double getCurrentStock() { return currentStock; }

    /**
     * Sets the current stock level.
     *
     * @param currentStock the current stock level to assign
     */
    public void setCurrentStock(double currentStock) { this.currentStock = currentStock; }

    /**
     * Returns the stock threshold that triggers reordering.
     *
     * @return the restock level
     */
    public double getRestockLevel() { return restockLevel; }

    /**
     * Sets the stock threshold that triggers reordering.
     *
     * @param restockLevel the restock level to assign
     */
    public void setRestockLevel(double restockLevel) { this.restockLevel = restockLevel; }

    /**
     * Returns the debit rate applied per unit used.
     *
     * @return the unit debit rate
     */
    public double getUnitDebitRate() { return unitDebitRate; }

    /**
     * Sets the debit rate applied per unit used.
     *
     * @param unitDebitRate the unit debit rate to assign
     */
    public void setUnitDebitRate(double unitDebitRate) { this.unitDebitRate = unitDebitRate; }

    /**
     * Returns whether the consumable is active.
     *
     * @return true if the consumable is active
     */
    public boolean isActive() { return active; }

    /**
     * Sets whether the consumable is active.
     *
     * @param active true to mark the consumable as active
     */
    public void setActive(boolean active) { this.active = active; }

    /**
     * Returns the derived inventory status label.
     *
     * @return the stock status label
     */
    public String getStockStatus() { return stockStatus; }

    /**
     * Sets the derived inventory status label.
     *
     * @param stockStatus the stock status label to assign
     */
    public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }

    /**
     * Returns the average daily consumption estimate.
     *
     * @return the average daily consumption value
     */
    public Double getAverageDailyConsumption() { return averageDailyConsumption; }

    /**
     * Sets the average daily consumption estimate.
     *
     * @param averageDailyConsumption the average daily consumption to assign
     */
    public void setAverageDailyConsumption(Double averageDailyConsumption) { this.averageDailyConsumption = averageDailyConsumption; }

    /**
     * Returns the projected days until depletion.
     *
     * @return the projected days until depletion
     */
    public Double getProjectedDaysUntilDepletion() { return projectedDaysUntilDepletion; }

    /**
     * Sets the projected days until depletion.
     *
     * @param projectedDaysUntilDepletion the projected days until depletion to assign
     */
    public void setProjectedDaysUntilDepletion(Double projectedDaysUntilDepletion) { this.projectedDaysUntilDepletion = projectedDaysUntilDepletion; }
}
