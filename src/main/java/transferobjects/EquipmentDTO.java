package transferobjects;

import java.time.LocalDateTime;

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
}
