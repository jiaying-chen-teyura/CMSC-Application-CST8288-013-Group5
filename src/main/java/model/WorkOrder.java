package model;

public class WorkOrder {
    private final int id;
    private final String description;
    private final String requestedBy;
    private String status; // "Pending", "In Progress"

    public WorkOrder(int id, String description, String requestedBy, String status) {
        this.id = id;
        this.description = description;
        this.requestedBy = requestedBy;
        this.status = status;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public String getRequestedBy() { return requestedBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
