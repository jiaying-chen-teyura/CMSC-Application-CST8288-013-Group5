package model;

public class Equipment {
    private final String assetTag;
    private final String name;
    private final String category;
    private String status; // "Available", "In-Use", "Down"

    public Equipment(String assetTag, String name, String category, String status) {
        this.assetTag = assetTag;
        this.name = name;
        this.category = category;
        this.status = status;
    }

    public String getAssetTag() { return assetTag; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
