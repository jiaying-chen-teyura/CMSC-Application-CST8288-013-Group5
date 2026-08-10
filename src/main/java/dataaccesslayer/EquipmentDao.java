package dataaccesslayer;

import java.util.List;
import transferobjects.ConsumableDTO;
import transferobjects.EquipmentDTO;

/**
 * Data Access Object (DAO) interface for equipment entities.
 * Provides methods for CRUD operations, status updates, and linked consumables.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */
public interface EquipmentDao {
    /**
     * Retrieves all equipment records.
     *
     * @return a list of all equipment records
     */
    List<EquipmentDTO> getAllEquipment();

    /**
     * Retrieves only active equipment records.
     *
     * @return a list of active equipment records
     */
    List<EquipmentDTO> getActiveEquipment();

    /**
     * Retrieves an equipment record by its asset tag.
     *
     * @param assetTag the asset tag to look up
     * @return the matching equipment record, or null if not found
     */
    EquipmentDTO getEquipmentByAssetTag(String assetTag);

    /**
     * Persists a new equipment record.
     *
     * @param equipment the equipment to add
     */
    void addEquipment(EquipmentDTO equipment);

    /**
     * Updates an existing equipment record.
     *
     * @param equipment the equipment data to update
     */
    void updateEquipment(EquipmentDTO equipment);

    /**
     * Updates the status of an equipment record.
     *
     * @param assetTag the equipment asset tag
     * @param status the new equipment status
     */
    void updateStatus(String assetTag, EquipmentDTO.Status status);

    /**
     * Marks an equipment record as inactive without deleting its history.
     *
     * @param assetTag the equipment asset tag to deactivate
     */
    void deleteEquipment(String assetTag);

    /**
     * Adds usage hours to an equipment record.
     *
     * @param assetTag the equipment asset tag
     * @param hours the number of hours to add
     */
    void addUsageHours(String assetTag, double hours);

    /**
     * Retrieves the consumables registered for a specific piece of equipment.
     *
     * @param assetTag the equipment asset tag
     * @return the consumables linked to the equipment
     */
    List<ConsumableDTO> getConsumablesForEquipment(String assetTag);

    /**
     * Replaces the full set of consumables linked to a piece of equipment.
     *
     * @param assetTag the equipment asset tag
     * @param consumableIds the consumable identifiers to associate with the equipment
     */
    void setEquipmentConsumables(String assetTag, List<Integer> consumableIds);
}
