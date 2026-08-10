package dataaccesslayer;

import java.util.List;
import transferobjects.ConsumableDTO;
import transferobjects.InventoryTransactionDTO;
import transferobjects.MaterialUsageDTO;

/**
 * Defines persistence operations for consumable inventory records.
 * Supports stock management, inventory transactions, and material usage tracking.
 * @author Le Bao Thach Nguyen 
 */
public interface ConsumableDao {
    /**
     * Retrieves all active consumables in the inventory.
     *
     * @return a list of consumables ordered for display or processing
     */
    List<ConsumableDTO> getAllConsumables();

    /**
     * Retrieves the inventory report for consumables, including stock status and depletion projections.
     *
     * @return a list of consumables with inventory analysis details
     */
    List<ConsumableDTO> getInventoryReport();

    /**
     * Retrieves a consumable by its unique identifier.
     *
     * @param consumableId the identifier of the consumable to retrieve
     * @return the matching consumable, or null if not found
     */
    ConsumableDTO getConsumableById(int consumableId);

    /**
     * Adds a new consumable to the inventory.
     *
     * @param consumable the consumable data to persist
     */
    void addConsumable(ConsumableDTO consumable);

    /**
     * Updates an existing consumable record.
     *
     * @param consumable the updated consumable data
     */
    void updateConsumable(ConsumableDTO consumable);

    /**
     * Soft-deletes a consumable while preserving its historical usage and transaction records.
     *
     * @param consumableId the identifier of the consumable to deactivate
     */
    void deleteConsumable(int consumableId);

    /**
     * Adjusts the current stock quantity for a consumable.
     *
     * @param consumableId the identifier of the consumable
     * @param delta the stock change to apply
     */
    void adjustStock(int consumableId, double delta);

    /**
     * Records an inventory transaction for a consumable.
     *
     * @param tx the transaction to persist
     */
    void recordInventoryTransaction(InventoryTransactionDTO tx);

    /**
     * Records material usage for a specific equipment usage session.
     *
     * @param usage the usage record to persist
     */
    void recordMaterialUsage(MaterialUsageDTO usage);

    /**
     * Retrieves material usage records associated with a usage session.
     *
     * @param usageSessionId the identifier of the usage session
     * @return a list of material usage entries for the session
     */
    List<MaterialUsageDTO> getMaterialUsageForSession(int usageSessionId);

    /**
     * Retrieves donation history for a specific user.
     *
     * @param userId the identifier of the user whose donations are requested
     * @return a list of donation transactions for the user
     */
    List<InventoryTransactionDTO> getDonationsForUser(int userId);
}
