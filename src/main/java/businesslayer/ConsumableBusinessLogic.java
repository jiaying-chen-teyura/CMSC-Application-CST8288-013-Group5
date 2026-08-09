package businesslayer;

import java.util.List;
import businesslayer.strategy.CreditContext;
import businesslayer.strategy.DonationCreditStrategy;
import dataaccesslayer.*;
import transferobjects.*;

/** Backs FR-04 (Monitoring Consumables & Inventory) and the "Donate Materials" use case. */
public class ConsumableBusinessLogic {

    private final ConsumableDao consumableDao;
    private final LedgerDao ledgerDao;

    public ConsumableBusinessLogic() {
        this(new ConsumableDaoImpl(), new LedgerDaoImpl());
    }

    public ConsumableBusinessLogic(ConsumableDao consumableDao, LedgerDao ledgerDao) {
        this.consumableDao = consumableDao;
        this.ledgerDao = ledgerDao;
    }

    public List<ConsumableDTO> getAllConsumables() {
        return consumableDao.getAllConsumables();
    }

    public ConsumableDTO getById(int consumableId) {
        return consumableDao.getConsumableById(consumableId);
    }

    /** FR-04: current stock level, consumption rate and projected time until depletion, for every material. */
    public List<ConsumableDTO> getInventoryReport() {
        return consumableDao.getInventoryReport();
    }

    /** FR-04: register a new consumable/material type (Shop-Tech only - enforced in the command layer). */
    public ConsumableDTO registerConsumable(String materialName, ConsumableDTO.Unit unit, double currentStock,
                                             double restockLevel, double unitDebitRate) throws ValidationException {
        ConsumableValidation.validateForRegistration(materialName, unit, restockLevel, unitDebitRate);

        ConsumableDTO consumable = new ConsumableDTO();
        consumable.setMaterialName(materialName);
        consumable.setUnit(unit);
        consumable.setCurrentStock(currentStock);
        consumable.setRestockLevel(restockLevel);
        consumable.setUnitDebitRate(unitDebitRate);
        consumableDao.addConsumable(consumable);
        return consumable;
    }

    /** FR-04: edit an existing consumable's details (Shop-Tech only - enforced in the command layer). */
    public void editConsumable(ConsumableDTO consumable) throws ValidationException {
        ConsumableValidation.validateForRegistration(consumable.getMaterialName(), consumable.getUnit(),
                consumable.getRestockLevel(), consumable.getUnitDebitRate());
        consumableDao.updateConsumable(consumable);
    }

    /** FR-04: retire a consumable (soft delete, Shop-Tech only - enforced in the command layer). */
    public void deleteConsumable(int consumableId) {
        consumableDao.deleteConsumable(consumableId);
    }

    /** Result of a donation, so the caller can show the member feedback (FR contributions). */
    public static class DonationResult {
        public final String materialName;
        public final double quantity;
        public final String unit;
        public final double creditEarned;
        public DonationResult(String materialName, double quantity, String unit, double creditEarned) {
            this.materialName = materialName;
            this.quantity = quantity;
            this.unit = unit;
            this.creditEarned = creditEarned;
        }
    }

    public DonationResult donateMaterial(int userId, int consumableId, double quantity) throws ValidationException {
        if (quantity <= 0) throw new ValidationException("Donated quantity must be positive.");
        ConsumableDTO consumable = consumableDao.getConsumableById(consumableId);
        if (consumable == null) throw new ValidationException("Unknown consumable.");

        consumableDao.adjustStock(consumableId, quantity);

        // Strategy pattern (required pattern): donation credit math is swappable independent of everything else.
        CreditContext creditContext = new CreditContext(new DonationCreditStrategy(consumable.getUnitDebitRate() * 0.5));
        double credit = creditContext.computeCredit(quantity);

        InventoryTransactionDTO tx = new InventoryTransactionDTO();
        tx.setConsumableId(consumableId);
        tx.setTransactionType(InventoryTransactionDTO.TransactionType.DONATION);
        tx.setQuantityChange(quantity);
        tx.setPerformedBy(userId);
        tx.setNotes("Member donation");
        tx.setCreditEarned(credit);
        consumableDao.recordInventoryTransaction(tx);

        AccountTransactionDTO creditTx = new AccountTransactionDTO();
        creditTx.setUserId(userId);
        creditTx.setTransactionType(AccountTransactionDTO.TransactionType.CREDIT);
        creditTx.setActivityType(AccountTransactionDTO.ActivityType.DONATION);
        creditTx.setAmount(credit);
        creditTx.setDescription("Donated " + quantity + " " + consumable.getUnit() + " of " + consumable.getMaterialName());
        ledgerDao.recordTransaction(creditTx);

        return new DonationResult(consumable.getMaterialName(), quantity, consumable.getUnit().name(), credit);
    }

    /** Donation history/record for a member, so they get visible feedback after donating (FR contributions). */
    public List<InventoryTransactionDTO> getDonationsForUser(int userId) {
        return consumableDao.getDonationsForUser(userId);
    }
}
