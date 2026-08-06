package businesslayer;

import java.util.List;
import businesslayer.builder.EquipmentBuilder;
import businesslayer.domain.EquipmentProfile;
import businesslayer.factory.EquipmentFactory;
import dataaccesslayer.EquipmentDao;
import dataaccesslayer.EquipmentDaoImpl;
import dataaccesslayer.MaintenanceDao;
import dataaccesslayer.MaintenanceDaoImpl;
import transferobjects.EquipmentComponentDTO;
import transferobjects.EquipmentDTO;

/** Backs FR-02 (Equipment & Resource Management). */
public class EquipmentBusinessLogic {

    private final EquipmentDao equipmentDao;
    private final MaintenanceDao maintenanceDao;

    public EquipmentBusinessLogic() {
        this(new EquipmentDaoImpl(), new MaintenanceDaoImpl());
    }

    public EquipmentBusinessLogic(EquipmentDao equipmentDao, MaintenanceDao maintenanceDao) {
        this.equipmentDao = equipmentDao;
        this.maintenanceDao = maintenanceDao;
    }

    public List<EquipmentDTO> getAllEquipment() {
        return equipmentDao.getAllEquipment();
    }

    public List<EquipmentDTO> getActiveEquipment() {
        return equipmentDao.getActiveEquipment();
    }

    public EquipmentDTO getByAssetTag(String assetTag) {
        return equipmentDao.getEquipmentByAssetTag(assetTag);
    }

    public EquipmentDTO registerEquipment(String assetTag, String make, String model, EquipmentDTO.Category category,
                                           String equipmentName, double accessCreditRate, String location,
                                           int registeredBy) throws ValidationException {
        EquipmentValidation.validateForRegistration(assetTag, make, model, category, equipmentName, accessCreditRate);
        if (equipmentDao.getEquipmentByAssetTag(assetTag) != null) {
            throw new ValidationException("An equipment record with that asset tag already exists.");
        }

        // Builder pattern assembles the DTO...
        EquipmentDTO equipment = new EquipmentBuilder()
                .assetTag(assetTag)
                .make(make)
                .model(model)
                .category(category)
                .equipmentName(equipmentName)
                .accessCreditRate(accessCreditRate)
                .location(location)
                .registeredBy(registeredBy)
                .status(EquipmentDTO.Status.AVAILABLE)
                .build();
        equipmentDao.addEquipment(equipment);

        // ...Simple Factory supplies the category-specific wear components to seed for FR-05.
        EquipmentProfile profile = EquipmentFactory.createProfile(category);
        for (EquipmentProfile.ComponentTemplate t : profile.defaultComponents()) {
            EquipmentComponentDTO comp = new EquipmentComponentDTO();
            comp.setAssetTag(assetTag);
            comp.setComponentName(t.name());
            comp.setMaintenanceThresholdHours(t.thresholdHours());
            maintenanceDao.addComponent(comp);
        }
        return equipment;
    }

    public void editEquipment(EquipmentDTO equipment) throws ValidationException {
        EquipmentValidation.validateForRegistration(equipment.getAssetTag(), equipment.getMake(), equipment.getModel(),
                equipment.getCategory(), equipment.getEquipmentName(), equipment.getAccessCreditRate());
        equipmentDao.updateEquipment(equipment);
    }

    public void deleteEquipment(String assetTag) {
        equipmentDao.deleteEquipment(assetTag);
    }
}
