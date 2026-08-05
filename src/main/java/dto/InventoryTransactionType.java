package dto;

/**
 * Mirrors the {@code transaction_type} ENUM on the
 * {@code inventory_transactions} table defined in
 * {@code src/main/resources/Database/CMSC_database.sql}:
 * <pre>
 * transaction_type ENUM('RESTOCK','DONATION','USAGE','ADJUSTMENT') NOT NULL
 * </pre>
 * Using a Java enum instead of a raw {@code String} ensures an invalid
 * transaction type is caught at compile time in the Business/DAO layers
 * rather than failing silently against the database constraint at runtime.
 *
 * @author Le Bao Thach Nguyen
 * @version 1.0
 */
public enum InventoryTransactionType {
    RESTOCK,
    DONATION,
    USAGE,
    ADJUSTMENT
}
