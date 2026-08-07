package businesslayer.domain;

import java.util.List;

public class ThreeDPrinterProfile extends EquipmentProfile {
    @Override
    public List<ComponentTemplate> defaultComponents() {
        return List.of(
            new ComponentTemplate("Nozzle", 150),
            new ComponentTemplate("Belt", 400),
            new ComponentTemplate("Build Plate", 800)
        );
    }

    @Override
    public String defaultCategoryLabel() { return "3D Printer"; }
}
