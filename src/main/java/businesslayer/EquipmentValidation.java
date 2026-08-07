package businesslayer;

import transferobjects.EquipmentDTO;

public class EquipmentValidation {

    private EquipmentValidation() { }

    public static void validateForRegistration(String assetTag, String make, String model,
                                                 EquipmentDTO.Category category, String equipmentName,
                                                 double accessCreditRate) throws ValidationException {
        if (assetTag == null || assetTag.isBlank()) throw new ValidationException("Asset tag is required.");
        if (make == null || make.isBlank()) throw new ValidationException("Make is required.");
        if (model == null || model.isBlank()) throw new ValidationException("Model is required.");
        if (category == null) throw new ValidationException("Category is required.");
        if (equipmentName == null || equipmentName.isBlank()) throw new ValidationException("Equipment name is required.");
        if (accessCreditRate < 0) throw new ValidationException("Access credit rate cannot be negative.");
    }
}
