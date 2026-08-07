package businesslayer.domain;

import java.util.List;

public class CncMachineProfile extends EquipmentProfile {
    @Override
    public List<ComponentTemplate> defaultComponents() {
        return List.of(
            new ComponentTemplate("Spindle", 600),
            new ComponentTemplate("Cutting Bit", 100),
            new ComponentTemplate("Ball Screw", 1000)
        );
    }

    @Override
    public String defaultCategoryLabel() { return "CNC Machine"; }
}
