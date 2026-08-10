package transferobjects;

import java.time.LocalDateTime;

/**
 * Represents the consumption of a material during a usage session.
 * @author Le Bao Thach Nguyen 
 */
public class MaterialUsageDTO {
    private Integer materialUsageId;
    private Integer usageSessionId;
    private Integer consumableId;
    private double quantityUsed;
    private double unitRate;
    private double materialDebit;
    private LocalDateTime recordedAt;

    // Not persisted directly - convenience fields joined in by the DAO for the JSPs (FR-03/FR-04).
    private String materialName;
    private String unit;

    /**
     * Returns the unique identifier of the material usage record.
     *
     * @return the material usage identifier
     */
    public Integer getMaterialUsageId() { return materialUsageId; }

    /**
     * Sets the unique identifier of the material usage record.
     *
     * @param materialUsageId the material usage identifier to assign
     */
    public void setMaterialUsageId(Integer materialUsageId) { this.materialUsageId = materialUsageId; }

    /**
     * Returns the usage session identifier associated with this record.
     *
     * @return the usage session identifier
     */
    public Integer getUsageSessionId() { return usageSessionId; }

    /**
     * Sets the usage session identifier associated with this record.
     *
     * @param usageSessionId the usage session identifier to assign
     */
    public void setUsageSessionId(Integer usageSessionId) { this.usageSessionId = usageSessionId; }

    /**
     * Returns the consumable identifier used in this record.
     *
     * @return the consumable identifier
     */
    public Integer getConsumableId() { return consumableId; }

    /**
     * Sets the consumable identifier used in this record.
     *
     * @param consumableId the consumable identifier to assign
     */
    public void setConsumableId(Integer consumableId) { this.consumableId = consumableId; }

    /**
     * Returns the quantity of material used.
     *
     * @return the quantity used
     */
    public double getQuantityUsed() { return quantityUsed; }

    /**
     * Sets the quantity of material used.
     *
     * @param quantityUsed the quantity used to assign
     */
    public void setQuantityUsed(double quantityUsed) { this.quantityUsed = quantityUsed; }

    /**
     * Returns the unit rate used for the material consumption.
     *
     * @return the unit rate
     */
    public double getUnitRate() { return unitRate; }

    /**
     * Sets the unit rate used for the material consumption.
     *
     * @param unitRate the unit rate to assign
     */
    public void setUnitRate(double unitRate) { this.unitRate = unitRate; }

    /**
     * Returns the monetary debit for the material usage.
     *
     * @return the material debit amount
     */
    public double getMaterialDebit() { return materialDebit; }

    /**
     * Sets the monetary debit for the material usage.
     *
     * @param materialDebit the material debit amount to assign
     */
    public void setMaterialDebit(double materialDebit) { this.materialDebit = materialDebit; }

    /**
     * Returns the time when the usage was recorded.
     *
     * @return the recorded timestamp
     */
    public LocalDateTime getRecordedAt() { return recordedAt; }

    /**
     * Sets the time when the usage was recorded.
     *
     * @param recordedAt the recorded timestamp to assign
     */
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }

    /**
     * Returns the name of the material for display purposes.
     *
     * @return the material name
     */
    public String getMaterialName() { return materialName; }

    /**
     * Sets the name of the material for display purposes.
     *
     * @param materialName the material name to assign
     */
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    /**
     * Returns the unit label associated with the material.
     *
     * @return the unit label
     */
    public String getUnit() { return unit; }

    /**
     * Sets the unit label associated with the material.
     *
     * @param unit the unit label to assign
     */
    public void setUnit(String unit) { this.unit = unit; }
}
