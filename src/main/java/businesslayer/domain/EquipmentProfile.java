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
public abstract class EquipmentProfile {

    /** Component name -> maintenance threshold (hours) for a brand-new unit of this category. */
    public abstract List<ComponentTemplate> defaultComponents();

    public abstract String defaultCategoryLabel();

    public record ComponentTemplate(String name, double thresholdHours) { }
}
