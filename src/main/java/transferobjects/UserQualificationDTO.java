package transferobjects;

import java.time.LocalDateTime;

/**
 * Represents a user's qualification for an equipment category.
 *
 * A qualification is awarded after the user successfully completes
 * a required training session.
 *
 * @author Tianzhu Li
 */
public class UserQualificationDTO {

    /**
     * Defines the possible statuses of a user qualification.
     */
    public enum Status {
        ACTIVE,
        EXPIRED,
        REVOKED
    }

    private Integer qualificationId;
    private Integer userId;
    private EquipmentDTO.Category category;
    private Integer trainingSessionId;
    private LocalDateTime qualifiedAt;
    private LocalDateTime expiresAt;
    private Status qualificationStatus = Status.ACTIVE;

    /**
     * Returns the qualification's unique identifier.
     *
     * @return the qualification ID
     */
    public Integer getQualificationId() {
        return qualificationId;
    }

    /**
     * Sets the qualification's unique identifier.
     *
     * @param qualificationId the qualification ID
     */
    public void setQualificationId(Integer qualificationId) {
        this.qualificationId = qualificationId;
    }

    /**
     * Returns the identifier of the qualified user.
     *
     * @return the user ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the identifier of the qualified user.
     *
     * @param userId the user ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Returns the equipment category covered by the qualification.
     *
     * @return the equipment category
     */
    public EquipmentDTO.Category getCategory() {
        return category;
    }

    /**
     * Sets the equipment category covered by the qualification.
     *
     * @param category the equipment category
     */
    public void setCategory(EquipmentDTO.Category category) {
        this.category = category;
    }

    /**
     * Returns the training session that awarded the qualification.
     *
     * @return the training session ID
     */
    public Integer getTrainingSessionId() {
        return trainingSessionId;
    }

    /**
     * Sets the training session that awarded the qualification.
     *
     * @param trainingSessionId the training session ID
     */
    public void setTrainingSessionId(Integer trainingSessionId) {
        this.trainingSessionId = trainingSessionId;
    }

    /**
     * Returns when the qualification was awarded.
     *
     * @return the qualification date and time
     */
    public LocalDateTime getQualifiedAt() {
        return qualifiedAt;
    }

    /**
     * Sets when the qualification was awarded.
     *
     * @param qualifiedAt the qualification date and time
     */
    public void setQualifiedAt(LocalDateTime qualifiedAt) {
        this.qualifiedAt = qualifiedAt;
    }

    /**
     * Returns when the qualification expires.
     *
     * @return the expiration date and time, or null if it does not expire
     */
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    /**
     * Sets when the qualification expires.
     *
     * @param expiresAt the expiration date and time
     */
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * Returns the current qualification status.
     *
     * @return the qualification status
     */
    public Status getQualificationStatus() {
        return qualificationStatus;
    }

    /**
     * Sets the current qualification status.
     *
     * @param qualificationStatus the qualification status
     */
    public void setQualificationStatus(
            Status qualificationStatus) {

        this.qualificationStatus = qualificationStatus;
    }
}