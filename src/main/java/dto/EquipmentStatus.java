package dto;

/**
 * Mirrors the {@code status} ENUM on the {@code equipment} table defined
 * in {@code src/main/resources/Database/CMSC_database.sql}:
 * <pre>
 * status ENUM('AVAILABLE','IN_USE','UNAVAILABLE','MAINTENANCE')
 *     NOT NULL DEFAULT 'AVAILABLE'
 * </pre>
 * This is also exactly the status vocabulary shown on the Equipment Status
 * Board wireframe (FR-03) — {@code AVAILABLE}, {@code IN_USE}, and
 * {@code MAINTENANCE} all appear there, with {@code UNAVAILABLE} covering
 * the soft-retired case used by {@link dao.EquipmentDao#deactivate}.
 *
 * @author Oladimeji Durojaiye
 * @version 1.0
 */
public enum EquipmentStatus {
    AVAILABLE,
    IN_USE,
    UNAVAILABLE,
    MAINTENANCE
}
