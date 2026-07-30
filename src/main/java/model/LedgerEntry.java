package model;

public class LedgerEntry {
    private final String entryDate;
    private final String type; // "Credit" or "Debit"
    private final double amount;
    private final String description;

    public LedgerEntry(String entryDate, String type, double amount, String description) {
        this.entryDate = entryDate;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    public String getEntryDate() { return entryDate; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
}
