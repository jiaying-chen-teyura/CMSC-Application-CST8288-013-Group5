package model;

import java.util.ArrayList;
import java.util.List;

public class ConsumableDao {

    private static final List<Consumable> CONSUMABLES = new ArrayList<>(List.of(
        new Consumable("PLA Filament (1.75mm)", 4200, "g"),
        new Consumable("Resin (standard)",       800,  "ml"),
        new Consumable("Plywood 1/8in sheets",   12,   "sheets")
    ));

    public List<Consumable> getAllConsumables() {
        return CONSUMABLES;
    }

    public boolean donate(String name, int amount) {
        for (Consumable c : CONSUMABLES) {
            if (c.getName().equals(name)) {
                c.addStock(amount);
                return true;
            }
        }
        return false;
    }
}
