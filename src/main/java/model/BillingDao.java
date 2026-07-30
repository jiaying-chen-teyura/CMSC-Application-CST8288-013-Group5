package model;

import java.util.ArrayList;
import java.util.List;

public class BillingDao {

    private static final List<LedgerEntry> ENTRIES = new ArrayList<>(List.of(
        new LedgerEntry("2026-07-05", "Credit", 15.00, "Donated 500g PLA filament"),
        new LedgerEntry("2026-07-12", "Debit",   8.50, "3D printer access - 2 hours"),
        new LedgerEntry("2026-07-20", "Debit",   4.25, "Filament used - project print")
    ));

    public List<LedgerEntry> getAllEntries() {
        return ENTRIES;
    }

    public double getBalance() {
        double balance = 0;
        for (LedgerEntry e : ENTRIES) {
            balance += "Credit".equals(e.getType()) ? e.getAmount() : -e.getAmount();
        }
        return balance;
    }

    public void settleDebits() {
        double owed = 0;
        for (LedgerEntry e : ENTRIES) {
            if ("Debit".equals(e.getType())) {
                owed += e.getAmount();
            }
        }
        if (owed > 0) {
            ENTRIES.add(new LedgerEntry("2026-07-29", "Credit", owed, "Debit settlement payment"));
        }
    }
}
