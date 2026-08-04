package dto;

/**
 * Mirrors the {@code unit} ENUM on the {@code consumables} table defined
 * in {@code src/main/resources/Database/CMSC_database.sql}:
 * <pre>
 * unit ENUM('GRAM','MILLILITRE','SHEET','PIECE') NOT NULL
 * </pre>
 * Using a Java enum instead of a raw {@code String} means an invalid
 * unit value is caught at compile time in the Business/DAO layers
 * instead of surfacing as a runtime SQL error only when it hits the
 * database's ENUM constraint.
 *
 * @author Le Bao Thach Nguyen
 * @version 1.0
 */
public enum ConsumableUnit {
    GRAM,
    MILLILITRE,
    SHEET,
    PIECE
}
