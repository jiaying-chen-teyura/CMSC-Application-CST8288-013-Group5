package transferobjects;

import java.time.LocalDateTime;

/**
 * Represents a maintenance or service work order and its associated details.
 * @author Le Bao Thach Nguyen 
 */
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
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    // convenience for JSP display
    private String requesterLabel;

    /**
     * Returns the unique identifier of the work order.
     *
     * @return the work order identifier
     */
    public Integer getWorkOrderId() { return workOrderId; }

    /**
     * Sets the unique identifier of the work order.
     *
     * @param workOrderId the work order identifier to assign
     */
    public void setWorkOrderId(Integer workOrderId) { this.workOrderId = workOrderId; }

    /**
     * Returns the client identifier associated with the work order.
     *
     * @return the client identifier
     */
    public Integer getClientId() { return clientId; }

    /**
     * Sets the client identifier associated with the work order.
     *
     * @param clientId the client identifier to assign
     */
    public void setClientId(Integer clientId) { this.clientId = clientId; }

    /**
     * Returns the member user identifier who submitted the work order.
     *
     * @return the member user identifier
     */
    public Integer getMemberUserId() { return memberUserId; }

    /**
     * Sets the member user identifier who submitted the work order.
     *
     * @param memberUserId the member user identifier to assign
     */
    public void setMemberUserId(Integer memberUserId) { this.memberUserId = memberUserId; }

    /**
     * Returns the shop technician assigned to the work order.
     *
     * @return the assigned shop technician identifier
     */
    public Integer getAssignedShopTechId() { return assignedShopTechId; }

    /**
     * Sets the shop technician assigned to the work order.
     *
     * @param assignedShopTechId the assigned shop technician identifier to assign
     */
    public void setAssignedShopTechId(Integer assignedShopTechId) { this.assignedShopTechId = assignedShopTechId; }

    /**
     * Returns the work order description.
     *
     * @return the description text
     */
    public String getDescription() { return description; }

    /**
     * Sets the work order description.
     *
     * @param description the description text to assign
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Returns the priority level of the work order.
     *
     * @return the priority value
     */
    public Priority getPriority() { return priority; }

    /**
     * Sets the priority level of the work order.
     *
     * @param priority the priority value to assign
     */
    public void setPriority(Priority priority) { this.priority = priority; }

    /**
     * Returns the current status of the work order.
     *
     * @return the work order status
     */
    public Status getStatus() { return status; }

    /**
     * Sets the current status of the work order.
     *
     * @param status the work order status to assign
     */
    public void setStatus(Status status) { this.status = status; }

    /**
     * Returns the estimated equipment cost.
     *
     * @return the estimated equipment cost
     */
    public double getEstimatedEquipmentCost() { return estimatedEquipmentCost; }

    /**
     * Sets the estimated equipment cost.
     *
     * @param estimatedEquipmentCost the estimated equipment cost to assign
     */
    public void setEstimatedEquipmentCost(double estimatedEquipmentCost) { this.estimatedEquipmentCost = estimatedEquipmentCost; }

    /**
     * Returns the estimated material cost.
     *
     * @return the estimated material cost
     */
    public double getEstimatedMaterialCost() { return estimatedMaterialCost; }

    /**
     * Sets the estimated material cost.
     *
     * @param estimatedMaterialCost the estimated material cost to assign
     */
    public void setEstimatedMaterialCost(double estimatedMaterialCost) { this.estimatedMaterialCost = estimatedMaterialCost; }

    /**
     * Returns the estimated labor cost.
     *
     * @return the estimated labor cost
     */
    public double getEstimatedLabourCost() { return estimatedLabourCost; }

    /**
     * Sets the estimated labor cost.
     *
     * @param estimatedLabourCost the estimated labor cost to assign
     */
    public void setEstimatedLabourCost(double estimatedLabourCost) { this.estimatedLabourCost = estimatedLabourCost; }

    /**
     * Returns the quoted price for the work order.
     *
     * @return the quoted price
     */
    public Double getQuotedPrice() { return quotedPrice; }

    /**
     * Sets the quoted price for the work order.
     *
     * @param quotedPrice the quoted price to assign
     */
    public void setQuotedPrice(Double quotedPrice) { this.quotedPrice = quotedPrice; }

    /**
     * Returns the credit earned upon completion.
     *
     * @return the earned credit
     */
    public double getCreditEarned() { return creditEarned; }

    /**
     * Sets the credit earned upon completion.
     *
     * @param creditEarned the credit to assign
     */
    public void setCreditEarned(double creditEarned) { this.creditEarned = creditEarned; }

    /**
     * Returns whether the client agreement has been accepted.
     *
     * @return true if the agreement has been accepted
     */
    public boolean isAgreementAccepted() { return agreementAccepted; }

    /**
     * Sets whether the client agreement has been accepted.
     *
     * @param agreementAccepted true if the agreement has been accepted
     */
    public void setAgreementAccepted(boolean agreementAccepted) { this.agreementAccepted = agreementAccepted; }

    /**
     * Returns the timestamp when the agreement was accepted.
     *
     * @return the agreement acceptance timestamp
     */
    public LocalDateTime getAgreementAcceptedAt() { return agreementAcceptedAt; }

    /**
     * Sets the timestamp when the agreement was accepted.
     *
     * @param agreementAcceptedAt the agreement acceptance timestamp to assign
     */
    public void setAgreementAcceptedAt(LocalDateTime agreementAcceptedAt) { this.agreementAcceptedAt = agreementAcceptedAt; }

    /**
     * Returns the submission timestamp of the work order.
     *
     * @return the submission timestamp
     */
    public LocalDateTime getSubmittedAt() { return submittedAt; }

    /**
     * Sets the submission timestamp of the work order.
     *
     * @param submittedAt the submission timestamp to assign
     */
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    /**
     * Returns the start timestamp of the work order.
     *
     * @return the start timestamp
     */
    public LocalDateTime getStartedAt() { return startedAt; }

    /**
     * Sets the start timestamp of the work order.
     *
     * @param startedAt the start timestamp to assign
     */
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    /**
     * Returns the completion timestamp of the work order.
     *
     * @return the completion timestamp
     */
    public LocalDateTime getCompletedAt() { return completedAt; }

    /**
     * Sets the completion timestamp of the work order.
     *
     * @param completedAt the completion timestamp to assign
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    /**
     * Returns the requester label used for display in the UI.
     *
     * @return the requester label
     */
    public String getRequesterLabel() { return requesterLabel; }

    /**
     * Sets the requester label used for display in the UI.
     *
     * @param requesterLabel the requester label to assign
     */
    public void setRequesterLabel(String requesterLabel) { this.requesterLabel = requesterLabel; }
}
