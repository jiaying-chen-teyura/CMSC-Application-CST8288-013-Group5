package businesslayer;

import java.util.ArrayList;
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

/**
 * Business logic for the Equipment & Resource Management feature (FR-02).
 * @author Oladimeji Durojaiye
 * @version 1.0
 */

/** Backs FR-02 (Equipment & Resource Management). */
public class EquipmentBusinessLogic {

    private final EquipmentDao equipmentDao;
    private final MaintenanceDao maintenanceDao;

    public EquipmentBusinessLogic() {
        this(new EquipmentDaoImpl(), new MaintenanceDaoImpl());
    }

    /**
     * Constructor for dependency injection, primarily for unit testing.
     * @param equipmentDao the EquipmentDao to use
     * @param maintenanceDao the MaintenanceDao to use
     */
    public EquipmentBusinessLogic(EquipmentDao equipmentDao, MaintenanceDao maintenanceDao) {
        this.equipmentDao = equipmentDao;
        this.maintenanceDao = maintenanceDao;
    }

    /**
     * Retrieves all equipment records from the database.
     * @return a list of EquipmentDTOs representing all equipment
     */
    public List<EquipmentDTO> getAllEquipment() {
        return equipmentDao.getAllEquipment();
    }

    /**
     * Retrieves all active equipment records from the database.
     * @return a list of EquipmentDTOs representing all active equipment
     */
    public List<EquipmentDTO> getActiveEquipment() {
        return equipmentDao.getActiveEquipment();
    }

    /**
     * Retrieves all equipment records along with their linked consumable types (FR-02) and their
     * key wear components (FR-05: nozzles, drive belts, laser tubes, etc., with current usage
     * hours against the maintenance threshold), for the Manage Equipment screen. Kept separate
     * from getAllEquipment() so the extra per-row lookups only happen where this detail is
     * actually displayed.
     * @return a list of EquipmentDTOs with consumableTypes and components populated
     */
    public List<EquipmentDTO> getAllEquipmentWithConsumables() {
        List<EquipmentDTO> list = equipmentDao.getAllEquipment();
        for (EquipmentDTO e : list) {
            e.setConsumableTypes(equipmentDao.getConsumablesForEquipment(e.getAssetTag()));
            e.setComponents(maintenanceDao.getComponentsForEquipment(e.getAssetTag()));
        }
        return list;
    }

    /** FR-02 "Consumable type": the consumable(s) a piece of equipment is registered to use. */
    public List<transferobjects.ConsumableDTO> getConsumablesForEquipment(String assetTag) {
        return equipmentDao.getConsumablesForEquipment(assetTag);
    }

    /**
     * Retrieves an equipment record by its asset tag.
     * @param assetTag the unique asset tag of the equipment
     * @return an EquipmentDTO representing the equipment, or null if not found
     */
    public EquipmentDTO getByAssetTag(String assetTag) {
        return equipmentDao.getEquipmentByAssetTag(assetTag);
    }
/**
     * Registers a new equipment record, and seeds its wear components for FR-05.
     * @param assetTag the unique asset tag of the equipment
     * @param make the manufacturer of the equipment
     * @param model the model of the equipment
     * @param category the category of the equipment
     * @param equipmentName the user-friendly name of the equipment
     * @param accessCreditRate the access credit rate of the equipment
     * @param location the location of the equipment
     * @param registeredBy the ID of the user registering this equipment
     * @param consumableIds the consumable type IDs this equipment is registered to use (FR-02)
     * @return an EquipmentDTO representing the newly registered equipment
     * @throws ValidationException if any validation rules are violated
     */
    public EquipmentDTO registerEquipment(String assetTag, String make, String model, EquipmentDTO.Category category,
                                           String equipmentName, double accessCreditRate, String location,
                                           int registeredBy, List<Integer> consumableIds) throws ValidationException {
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
        equipmentDao.setEquipmentConsumables(assetTag, consumableIds != null ? consumableIds : new ArrayList<>());

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

    /**
     * Edits an existing equipment record.
     * @param equipment the EquipmentDTO containing updated information
     * @param consumableIds the consumable type IDs this equipment should be linked to (FR-02)
     * @throws ValidationException if any validation rules are violated
     */
    public void editEquipment(EquipmentDTO equipment, List<Integer> consumableIds) throws ValidationException {
        EquipmentValidation.validateForRegistration(equipment.getAssetTag(), equipment.getMake(), equipment.getModel(),
                equipment.getCategory(), equipment.getEquipmentName(), equipment.getAccessCreditRate());
        equipmentDao.updateEquipment(equipment);
        equipmentDao.setEquipmentConsumables(equipment.getAssetTag(), consumableIds != null ? consumableIds : new ArrayList<>());
    }

    /**
     * Deletes an equipment record by its asset tag.
     * @param assetTag the unique asset tag of the equipment to delete
     */
    public void deleteEquipment(String assetTag) {
        equipmentDao.deleteEquipment(assetTag);
    }
}
