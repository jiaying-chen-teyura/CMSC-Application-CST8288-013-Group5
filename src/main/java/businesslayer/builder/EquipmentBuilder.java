package businesslayer.builder;

import transferobjects.EquipmentDTO;

/**
 * Builder Pattern (required pattern).
 * EquipmentDTO has one required identity field (assetTag) plus many
 * optional/derived fields (status, usage hours, active flag...). Registering
 * a new machine (FR-02) only ever needs to set a handful of them, so a
 * fluent builder keeps RegisterEquipmentCommand / EquipmentBusinessLogic
 * from calling a long telescoping constructor or a pile of setters.
 *
 * Used by: EquipmentBusinessLogic.registerEquipment(...)
 */
public class EquipmentBuilder {

    private final EquipmentDTO equipment = new EquipmentDTO();

    public EquipmentBuilder assetTag(String assetTag) {
        equipment.setAssetTag(assetTag);
        return this;
    }

    public EquipmentBuilder make(String make) {
        equipment.setMake(make);
        return this;
    }

    public EquipmentBuilder model(String model) {
        equipment.setModel(model);
        return this;
    }

    public EquipmentBuilder category(EquipmentDTO.Category category) {
        equipment.setCategory(category);
        return this;
    }

    public EquipmentBuilder equipmentName(String equipmentName) {
        equipment.setEquipmentName(equipmentName);
        return this;
    }

    public EquipmentBuilder status(EquipmentDTO.Status status) {
        equipment.setStatus(status);
        return this;
    }

    public EquipmentBuilder accessCreditRate(double rate) {
        equipment.setAccessCreditRate(rate);
        return this;
    }

    public EquipmentBuilder location(String location) {
        equipment.setLocation(location);
        return this;
    }

    public EquipmentBuilder registeredBy(int userId) {
        equipment.setRegisteredBy(userId);
        return this;
    }

    public EquipmentDTO build() {
        if (equipment.getAssetTag() == null || equipment.getAssetTag().isBlank()) {
            throw new IllegalStateException("EquipmentBuilder: assetTag is required");
        }
        if (equipment.getCategory() == null) {
            throw new IllegalStateException("EquipmentBuilder: category is required");
        }
        if (equipment.getStatus() == null) {
            equipment.setStatus(EquipmentDTO.Status.AVAILABLE);
        }
        return equipment;
    }
}
