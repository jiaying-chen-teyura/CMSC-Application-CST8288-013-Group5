package transferobjects;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) for Equipment entities.
 * Represents the data structure for equipment, including asset tag, make, model, category, status, access credit rate, total usage hours, location, and registration details.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */

public class EquipmentDTO {

    public enum Category { THREE_D_PRINTER, LASER_CUTTER, CNC }
    public enum Status { AVAILABLE, IN_USE, UNAVAILABLE, MAINTENANCE }

    private String assetTag;
    private String make;
    private String model;
    private Category category;
    private String equipmentName;
    private Status status = Status.AVAILABLE;
    private double accessCreditRate;
    private double totalUsageHours;
    private String location;
    private Integer registeredBy;
    private LocalDateTime registeredAt;
    private boolean active = true;

    // Not persisted on the equipment row itself (FR-02's "consumable type") - the accepted consumable
    // types live in the equipment_consumables join table and are filled in by the business layer for the JSPs.
    private List<ConsumableDTO> consumableTypes;

    // Not persisted on the equipment row itself - the key wear components (FR-05, e.g. nozzle, drive
    // belt, laser tube) live in equipment_components and are filled in by the business layer for the JSPs.
    private List<EquipmentComponentDTO> components;

    // Not persisted, and NOT the same thing as `status` above. `status` answers "can a member book
    // this right now?" (AVAILABLE/IN_USE/UNAVAILABLE/MAINTENANCE). This answers "does it have an open
    // predictive-maintenance alert?" - a piece of equipment can still be AVAILABLE for booking while
    // this is true (the Shop-Tech has a grace window to schedule the work - see EquipmentComponentDTO).
    // Filled in by MaintenanceBusinessLogic.getEquipmentNeedingAttention() for the Shop-Tech's
    // "Equipment Needing Attention" screen so that view can show a distinct health status instead of
    // reusing the member-facing booking status.
    private boolean needsMaintenance = false;

    public EquipmentDTO() { }

    public String getAssetTag() { return assetTag; }
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public boolean isNeedsMaintenance() { return needsMaintenance; }
    public void setNeedsMaintenance(boolean needsMaintenance) { this.needsMaintenance = needsMaintenance; }

    public double getAccessCreditRate() { return accessCreditRate; }
    public void setAccessCreditRate(double accessCreditRate) { this.accessCreditRate = accessCreditRate; }

    public double getTotalUsageHours() { return totalUsageHours; }
    public void setTotalUsageHours(double totalUsageHours) { this.totalUsageHours = totalUsageHours; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getRegisteredBy() { return registeredBy; }
    public void setRegisteredBy(Integer registeredBy) { this.registeredBy = registeredBy; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public List<ConsumableDTO> getConsumableTypes() { return consumableTypes; }
    public void setConsumableTypes(List<ConsumableDTO> consumableTypes) { this.consumableTypes = consumableTypes; }

    public List<EquipmentComponentDTO> getComponents() { return components; }
    public void setComponents(List<EquipmentComponentDTO> components) { this.components = components; }
}
