package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object representing a single row of the {@code equipment}
 * table (see {@code src/main/resources/Database/CMSC_database.sql}).
 * <p>
 * Carries data only — no business logic, no persistence awareness, no
 * validation. Field-for-field mapping to the real schema:
 * <pre>
 * asset_tag            VARCHAR(30)   PRIMARY KEY   -&gt; assetTag
 * make                 VARCHAR(60)   NOT NULL      -&gt; make
 * model                VARCHAR(60)   NOT NULL      -&gt; model
 * category             ENUM(...)     NOT NULL      -&gt; category
 * equipment_name       VARCHAR(100)  NOT NULL      -&gt; equipmentName
 * status               ENUM(...)     NOT NULL      -&gt; status
 * access_credit_rate   DECIMAL(8,2)  NOT NULL      -&gt; accessCreditRate
 * total_usage_hours    DECIMAL(10,2) NOT NULL      -&gt; totalUsageHours
 * location             VARCHAR(100)  NULL          -&gt; location
 * registered_by        INT           NOT NULL      -&gt; registeredBy
 * registered_at        DATETIME      NOT NULL      -&gt; registeredAt
 * active               BOOLEAN       NOT NULL      -&gt; active
 * </pre>
 * {@code accessCreditRate} and {@code totalUsageHours} are {@link BigDecimal}
 * rather than {@code double} on purpose — the HLD's Data Integrity section
 * (8.1.3) calls out that "financial values use the DECIMAL data type to
 * avoid calculation errors," and {@code double} would reintroduce exactly
 * the floating-point rounding risk that column choice is meant to avoid.
 *
 * @author Oladimeji Durojaiye
 * @version 1.0
 */
public class EquipmentDTO {

    private String assetTag;
    private String make;
    private String model;
    private EquipmentCategory category;
    private String equipmentName;
    private EquipmentStatus status;
    private BigDecimal accessCreditRate;
    private BigDecimal totalUsageHours;
    private String location;
    private int registeredBy;
    private LocalDateTime registeredAt;
    private boolean active;

    /** No-arg constructor, required for JavaBean-style construction. */
    public EquipmentDTO() {
    }

    /**
     * Constructs an EquipmentDTO from the fields a servlet collects on the
     * "Register New Equipment" form (see {@code EquipmentService#registerEquipment}
     * for where {@code status}, {@code totalUsageHours}, {@code registeredAt},
     * and {@code active} get their defaults before insertion).
     *
     * @param assetTag         unique equipment identifier, e.g. {@code "3DP-001"}
     * @param make              manufacturer, e.g. {@code "Ultimaker"}
     * @param model             model name, e.g. {@code "S5"}
     * @param category          equipment category
     * @param equipmentName     display name shown on the status board
     * @param accessCreditRate  credit rate charged per hour of use
     * @param location          physical location in the maker space, may be {@code null}
     * @param registeredBy      user_id of the Shop-Tech/admin who registered this equipment
     */
    public EquipmentDTO(String assetTag, String make, String model, EquipmentCategory category,
            String equipmentName, BigDecimal accessCreditRate, String location, int registeredBy) {
        this.assetTag = assetTag;
        this.make = make;
        this.model = model;
        this.category = category;
        this.equipmentName = equipmentName;
        this.accessCreditRate = accessCreditRate;
        this.location = location;
        this.registeredBy = registeredBy;
    }

    /** @return the unique asset tag (primary key) */
    public String getAssetTag() {
        return assetTag;
    }

    /** @param assetTag the asset tag to set */
    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    /** @return the manufacturer */
    public String getMake() {
        return make;
    }

    /** @param make the manufacturer to set */
    public void setMake(String make) {
        this.make = make;
    }

    /** @return the model name */
    public String getModel() {
        return model;
    }

    /** @param model the model name to set */
    public void setModel(String model) {
        this.model = model;
    }

    /** @return the equipment category */
    public EquipmentCategory getCategory() {
        return category;
    }

    /** @param category the equipment category to set */
    public void setCategory(EquipmentCategory category) {
        this.category = category;
    }

    /** @return the display name shown on the status board */
    public String getEquipmentName() {
        return equipmentName;
    }

    /** @param equipmentName the display name to set */
    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    /** @return the current status */
    public EquipmentStatus getStatus() {
        return status;
    }

    /** @param status the status to set */
    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    /** @return the credit rate charged per hour of use */
    public BigDecimal getAccessCreditRate() {
        return accessCreditRate;
    }

    /** @param accessCreditRate the credit rate to set */
    public void setAccessCreditRate(BigDecimal accessCreditRate) {
        this.accessCreditRate = accessCreditRate;
    }

    /** @return the cumulative usage hours logged against this equipment */
    public BigDecimal getTotalUsageHours() {
        return totalUsageHours;
    }

    /** @param totalUsageHours the cumulative usage hours to set */
    public void setTotalUsageHours(BigDecimal totalUsageHours) {
        this.totalUsageHours = totalUsageHours;
    }

    /** @return the physical location in the maker space, may be {@code null} */
    public String getLocation() {
        return location;
    }

    /** @param location the physical location to set */
    public void setLocation(String location) {
        this.location = location;
    }

    /** @return the user_id of the Shop-Tech/admin who registered this equipment */
    public int getRegisteredBy() {
        return registeredBy;
    }

    /** @param registeredBy the registering user's id to set */
    public void setRegisteredBy(int registeredBy) {
        this.registeredBy = registeredBy;
    }

    /** @return the timestamp this equipment was registered */
    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    /** @param registeredAt the registration timestamp to set */
    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    /** @return whether this equipment is active (soft-delete flag) */
    public boolean isActive() {
        return active;
    }

    /** @param active the active flag to set */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Two EquipmentDTOs are equal if they share the same {@code assetTag},
     * since {@code asset_tag} is the table's primary key.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipmentDTO)) {
            return false;
        }
        EquipmentDTO that = (EquipmentDTO) o;
        return Objects.equals(assetTag, that.assetTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assetTag);
    }

    @Override
    public String toString() {
        return "EquipmentDTO{"
                + "assetTag='" + assetTag + '\''
                + ", make='" + make + '\''
                + ", model='" + model + '\''
                + ", category=" + category
                + ", equipmentName='" + equipmentName + '\''
                + ", status=" + status
                + ", accessCreditRate=" + accessCreditRate
                + ", totalUsageHours=" + totalUsageHours
                + ", location='" + location + '\''
                + ", registeredBy=" + registeredBy
                + ", registeredAt=" + registeredAt
                + ", active=" + active
                + '}';
    }
}
