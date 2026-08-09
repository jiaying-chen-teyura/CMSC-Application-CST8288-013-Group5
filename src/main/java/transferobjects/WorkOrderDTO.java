package transferobjects;

import java.time.LocalDateTime;

public class WorkOrderDTO {

    public enum Priority { STANDARD, RUSH }
    public enum Status { SUBMITTED, QUOTED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED }

    private Integer workOrderId;
    private Integer clientId;
    private Integer memberUserId;
    private Integer assignedShopTechId;
    private String description;
    private Priority priority = Priority.STANDARD;
    private Status status = Status.SUBMITTED;
    private double estimatedEquipmentCost;
    private double estimatedMaterialCost;
    private double estimatedLabourCost;
    private Double quotedPrice;
    private double creditEarned;
    private boolean agreementAccepted;
    private LocalDateTime agreementAcceptedAt;
    private LocalDateTime submittedAt;
    private LocalDateTime completedAt;

    // convenience for JSP display
    private String requesterLabel;

    public Integer getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Integer workOrderId) { this.workOrderId = workOrderId; }

    public Integer getClientId() { return clientId; }
    public void setClientId(Integer clientId) { this.clientId = clientId; }

    public Integer getMemberUserId() { return memberUserId; }
    public void setMemberUserId(Integer memberUserId) { this.memberUserId = memberUserId; }

    public Integer getAssignedShopTechId() { return assignedShopTechId; }
    public void setAssignedShopTechId(Integer assignedShopTechId) { this.assignedShopTechId = assignedShopTechId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public double getEstimatedEquipmentCost() { return estimatedEquipmentCost; }
    public void setEstimatedEquipmentCost(double estimatedEquipmentCost) { this.estimatedEquipmentCost = estimatedEquipmentCost; }

    public double getEstimatedMaterialCost() { return estimatedMaterialCost; }
    public void setEstimatedMaterialCost(double estimatedMaterialCost) { this.estimatedMaterialCost = estimatedMaterialCost; }

    public double getEstimatedLabourCost() { return estimatedLabourCost; }
    public void setEstimatedLabourCost(double estimatedLabourCost) { this.estimatedLabourCost = estimatedLabourCost; }

    public Double getQuotedPrice() { return quotedPrice; }
    public void setQuotedPrice(Double quotedPrice) { this.quotedPrice = quotedPrice; }

    public double getCreditEarned() { return creditEarned; }
    public void setCreditEarned(double creditEarned) { this.creditEarned = creditEarned; }

    public boolean isAgreementAccepted() { return agreementAccepted; }
    public void setAgreementAccepted(boolean agreementAccepted) { this.agreementAccepted = agreementAccepted; }

    public LocalDateTime getAgreementAcceptedAt() { return agreementAcceptedAt; }
    public void setAgreementAcceptedAt(LocalDateTime agreementAcceptedAt) { this.agreementAcceptedAt = agreementAcceptedAt; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public String getRequesterLabel() { return requesterLabel; }
    public void setRequesterLabel(String requesterLabel) { this.requesterLabel = requesterLabel; }
}
