package dataaccesslayer;

import java.util.List;
import transferobjects.ConsumableDTO;
import transferobjects.InventoryTransactionDTO;
import transferobjects.MaterialUsageDTO;

public interface ConsumableDao {
    List<ConsumableDTO> getAllConsumables();
    /** FR-04 inventory report: stock level, consumption rate, projected depletion. */
    List<ConsumableDTO> getInventoryReport();
    ConsumableDTO getConsumableById(int consumableId);
    /** FR-04 consumable management (Shop-Tech only, enforced in the business/command layers). */
    void addConsumable(ConsumableDTO consumable);
    void updateConsumable(ConsumableDTO consumable);
    /** Soft delete: consumables history (material usage, inventory transactions) must survive. */
    void deleteConsumable(int consumableId);
    void adjustStock(int consumableId, double delta);
    void recordInventoryTransaction(InventoryTransactionDTO tx);
    void recordMaterialUsage(MaterialUsageDTO usage);
    List<MaterialUsageDTO> getMaterialUsageForSession(int usageSessionId);
    /** Donation history for a member (material name joined in) so they get feedback after donating. */
    List<InventoryTransactionDTO> getDonationsForUser(int userId);
}
