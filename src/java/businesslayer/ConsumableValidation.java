package businesslayer;

import transferobjects.ConsumableDTO;

public class ConsumableValidation {

    private ConsumableValidation() { }

    public static void validateForRegistration(String materialName, ConsumableDTO.Unit unit,
                                                 double restockLevel, double unitDebitRate) throws ValidationException {
        if (materialName == null || materialName.isBlank()) throw new ValidationException("Material name is required.");
        if (unit == null) throw new ValidationException("Unit is required.");
        if (restockLevel < 0) throw new ValidationException("Restock level cannot be negative.");
        if (unitDebitRate < 0) throw new ValidationException("Unit debit rate cannot be negative.");
    }
}
