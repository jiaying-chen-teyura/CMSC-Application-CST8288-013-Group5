package businesslayer.domain;

import java.util.List;

public class LaserCutterProfile extends EquipmentProfile {
    @Override
    public List<ComponentTemplate> defaultComponents() {
        return List.of(
            new ComponentTemplate("Laser Tube", 500),
            new ComponentTemplate("Lens", 300),
            new ComponentTemplate("Belt/Rail", 600)
        );
    }

    @Override
    public String defaultCategoryLabel() { return "Laser Cutter"; }
}
