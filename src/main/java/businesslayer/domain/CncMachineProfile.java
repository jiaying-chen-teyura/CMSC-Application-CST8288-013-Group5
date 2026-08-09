package businesslayer.domain;

import java.util.List;

/**
 * Category-specific behaviour that isn't stored per-row in the equipment
 * table: which wear components a fresh machine of this category ships
 * with, and its default maintenance/component thresholds (FR-05). Produced
 * by EquipmentFactory (Simple Factory pattern) rather than persisted, since
 * it's policy, not data.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */

public class CncMachineProfile extends EquipmentProfile {
    @Override
    public List<ComponentTemplate> defaultComponents() {
        return List.of(
            new ComponentTemplate("Spindle", 600),
            new ComponentTemplate("Cutting Bit", 100),
            new ComponentTemplate("Ball Screw", 1000)
        );
    }

    /** Returns the default category label for CNC machines. */
    @Override
    public String defaultCategoryLabel() { return "CNC Machine"; }
}
