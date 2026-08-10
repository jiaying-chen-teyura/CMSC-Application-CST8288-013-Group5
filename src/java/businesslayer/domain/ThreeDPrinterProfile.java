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

public class ThreeDPrinterProfile extends EquipmentProfile {
    @Override
    public List<ComponentTemplate> defaultComponents() {
        return List.of(
            new ComponentTemplate("Nozzle", 150),
            new ComponentTemplate("Belt", 400),
            new ComponentTemplate("Build Plate", 800)
        );
    }
/** Returns the default category label for 3D printers. */
    @Override
    public String defaultCategoryLabel() { return "3D Printer"; }
}
