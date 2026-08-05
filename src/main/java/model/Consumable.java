package model;

public class Consumable {
    private final String name;
    private int stockLevel;
    private final String unit;

    public Consumable(String name, int stockLevel, String unit) {
        this.name = name;
        this.stockLevel = stockLevel;
        this.unit = unit;
    }

    public String getName() { return name; }
    public int getStockLevel() { return stockLevel; }
    public String getUnit() { return unit; }
    public void addStock(int amount) { this.stockLevel += amount; }
}
