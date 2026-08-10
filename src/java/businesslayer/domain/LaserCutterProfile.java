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

public class LaserCutterProfile extends EquipmentProfile {
    @Override
    public List<ComponentTemplate> defaultComponents() {
        return List.of(
            new ComponentTemplate("Laser Tube", 500),
            new ComponentTemplate("Lens", 300),
            new ComponentTemplate("Belt/Rail", 600)
        );
    }

    /** Returns the default category label for laser cutters. */
    @Override
    public String defaultCategoryLabel() { return "Laser Cutter"; }
}
