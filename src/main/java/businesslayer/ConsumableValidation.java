package businesslayer;

import transferobjects.ConsumableDTO;

/**
 * Provides validation logic for consumable registration and stock-related input.
 *  @author Le Bao Thach Nguyen 
 */
public class ConsumableValidation {

    private ConsumableValidation() { }

    /**
     * Validates the fields required to register a consumable item.
     *
     * @param materialName the name of the consumable
     * @param unit the unit of measurement used for the consumable
     * @param restockLevel the minimum stock threshold before reordering
     * @param unitDebitRate the debit rate applied per unit used
     * @throws ValidationException if any supplied value is invalid
     */
    public static void validateForRegistration(String materialName, ConsumableDTO.Unit unit,
                                                 double restockLevel, double unitDebitRate) throws ValidationException {
        if (materialName == null || materialName.isBlank()) throw new ValidationException("Material name is required.");
        if (unit == null) throw new ValidationException("Unit is required.");
        if (restockLevel < 0) throw new ValidationException("Restock level cannot be negative.");
        if (unitDebitRate < 0) throw new ValidationException("Unit debit rate cannot be negative.");
    }
}
