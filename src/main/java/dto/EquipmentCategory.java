package dto;

/**
 * Mirrors the {@code category} ENUM on the {@code equipment} table defined
 * in {@code src/main/resources/Database/CMSC_database.sql}:
 * <pre>
 * category ENUM('THREE_D_PRINTER','LASER_CUTTER','CNC') NOT NULL
 * </pre>
 * Using a Java enum instead of a raw {@code String} means an invalid
 * category value is caught at compile time in the Business/DAO layers
 * instead of surfacing as a runtime SQL error only when it hits the
 * database's ENUM constraint.
 *
 * @author Oladimeji Durojaiye
 * @version 1.0
 */
public enum EquipmentCategory {
    THREE_D_PRINTER,
    LASER_CUTTER,
    CNC
}
