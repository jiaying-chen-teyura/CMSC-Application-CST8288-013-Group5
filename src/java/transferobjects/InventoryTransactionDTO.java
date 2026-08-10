package transferobjects;

import java.time.LocalDateTime;

public class InventoryTransactionDTO {

    public enum TransactionType { RESTOCK, DONATION, USAGE, ADJUSTMENT }

    private Integer inventoryTransactionId;
    private Integer consumableId;
    private TransactionType transactionType;
    private double quantityChange;
    private Integer performedBy;
    private LocalDateTime transactionTime;
    private String notes;
    /** Transient - only populated by joined queries (e.g. getDonationsForUser) for display purposes; not a DB column here. */
    private String materialName;
    /** Credit earned for this transaction (currently only set for DONATION rows, via DonationCreditStrategy at donation time). */
    private Double creditEarned;

    public Integer getInventoryTransactionId() { return inventoryTransactionId; }
    public void setInventoryTransactionId(Integer inventoryTransactionId) { this.inventoryTransactionId = inventoryTransactionId; }

    public Integer getConsumableId() { return consumableId; }
    public void setConsumableId(Integer consumableId) { this.consumableId = consumableId; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public double getQuantityChange() { return quantityChange; }
    public void setQuantityChange(double quantityChange) { this.quantityChange = quantityChange; }

    public Integer getPerformedBy() { return performedBy; }
    public void setPerformedBy(Integer performedBy) { this.performedBy = performedBy; }

    public LocalDateTime getTransactionTime() { return transactionTime; }
    public void setTransactionTime(LocalDateTime transactionTime) { this.transactionTime = transactionTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public Double getCreditEarned() { return creditEarned; }
    public void setCreditEarned(Double creditEarned) { this.creditEarned = creditEarned; }
}
