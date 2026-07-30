package model;

public class MaintenanceAlert {
    private final String equipmentName;
    private final String issue;
    private String status; // "Open", "Scheduled"

    public MaintenanceAlert(String equipmentName, String issue, String status) {
        this.equipmentName = equipmentName;
        this.issue = issue;
        this.status = status;
    }

    public String getEquipmentName() { return equipmentName; }
    public String getIssue() { return issue; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
