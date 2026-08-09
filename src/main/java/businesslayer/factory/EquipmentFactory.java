package businesslayer.factory;

import businesslayer.domain.*;
import transferobjects.EquipmentDTO;

/**
 * Simple Factory Pattern (required pattern).
 * Given an EquipmentDTO.Category, returns the matching EquipmentProfile
 * (default wear components + thresholds). Used by
 * EquipmentBusinessLogic.registerEquipment(...) right after a new
 * EquipmentDTO is built (Builder pattern) so the correct
 * equipment_components rows get seeded automatically for FR-05 predictive
 * maintenance, without RegisterEquipmentCommand needing to know per-category
 * rules itself.
 */
public class EquipmentFactory {

    private EquipmentFactory() { }

    public static EquipmentProfile createProfile(EquipmentDTO.Category category) {
        return switch (category) {
            case THREE_D_PRINTER -> new ThreeDPrinterProfile();
            case LASER_CUTTER -> new LaserCutterProfile();
            case CNC -> new CncMachineProfile();
        };
    }
}
