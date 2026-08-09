package transferobjects;

import java.time.LocalDateTime;

public class MaterialUsageDTO {
    private Integer materialUsageId;
    private Integer usageSessionId;
    private Integer consumableId;
    private double quantityUsed;
    private double unitRate;
    private double materialDebit;
    private LocalDateTime recordedAt;

    public Integer getMaterialUsageId() { return materialUsageId; }
    public void setMaterialUsageId(Integer materialUsageId) { this.materialUsageId = materialUsageId; }

    public Integer getUsageSessionId() { return usageSessionId; }
    public void setUsageSessionId(Integer usageSessionId) { this.usageSessionId = usageSessionId; }

    public Integer getConsumableId() { return consumableId; }
    public void setConsumableId(Integer consumableId) { this.consumableId = consumableId; }

    public double getQuantityUsed() { return quantityUsed; }
    public void setQuantityUsed(double quantityUsed) { this.quantityUsed = quantityUsed; }

    public double getUnitRate() { return unitRate; }
    public void setUnitRate(double unitRate) { this.unitRate = unitRate; }

    public double getMaterialDebit() { return materialDebit; }
    public void setMaterialDebit(double materialDebit) { this.materialDebit = materialDebit; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}
