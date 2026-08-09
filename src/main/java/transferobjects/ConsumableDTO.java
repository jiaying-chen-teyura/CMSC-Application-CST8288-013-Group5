package transferobjects;

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

    public Integer getConsumableId() { return consumableId; }
    public void setConsumableId(Integer consumableId) { this.consumableId = consumableId; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    public double getCurrentStock() { return currentStock; }
    public void setCurrentStock(double currentStock) { this.currentStock = currentStock; }

    public double getRestockLevel() { return restockLevel; }
    public void setRestockLevel(double restockLevel) { this.restockLevel = restockLevel; }

    public double getUnitDebitRate() { return unitDebitRate; }
    public void setUnitDebitRate(double unitDebitRate) { this.unitDebitRate = unitDebitRate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getStockStatus() { return stockStatus; }
    public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }

    public Double getAverageDailyConsumption() { return averageDailyConsumption; }
    public void setAverageDailyConsumption(Double averageDailyConsumption) { this.averageDailyConsumption = averageDailyConsumption; }

    public Double getProjectedDaysUntilDepletion() { return projectedDaysUntilDepletion; }
    public void setProjectedDaysUntilDepletion(Double projectedDaysUntilDepletion) { this.projectedDaysUntilDepletion = projectedDaysUntilDepletion; }
}
