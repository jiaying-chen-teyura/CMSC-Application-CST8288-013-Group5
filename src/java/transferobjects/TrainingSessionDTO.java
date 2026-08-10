package transferobjects;

import java.time.LocalDateTime;

/**
 * Transfer object for a training session.
 * Stores the training session schedule, trainer, equipment category,
 * capacity, status, location, and trainer credit information.
 *
 * @author Tianzhu Li
 */
public class TrainingSessionDTO {

    /**
     * Represents the current status of a training session.
     */
    public enum Status {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    private Integer trainingSessionId;
    private Integer trainerId;
    private EquipmentDTO.Category category;
    private String title;
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private String location;
    private int capacity;
    private Status status = Status.SCHEDULED;
    private double trainerCredit;

    private String trainerName;

    /**
     * Returns the unique identifier of the training session.
     *
     * @return the training session ID
     */
    public Integer getTrainingSessionId() {
        return trainingSessionId;
    }

    /**
     * Sets the unique identifier of the training session.
     *
     * @param trainingSessionId the training session ID
     */
    public void setTrainingSessionId(Integer trainingSessionId) {
        this.trainingSessionId = trainingSessionId;
    }

    /**
     * Returns the ID of the trainer conducting the session.
     *
     * @return the trainer ID
     */
    public Integer getTrainerId() {
        return trainerId;
    }

    /**
     * Sets the ID of the trainer conducting the session.
     *
     * @param trainerId the trainer ID
     */
    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }

    /**
     * Returns the equipment category covered by the training session.
     *
     * @return the equipment category
     */
    public EquipmentDTO.Category getCategory() {
        return category;
    }

    /**
     * Sets the equipment category covered by the training session.
     *
     * @param category the equipment category
     */
    public void setCategory(EquipmentDTO.Category category) {
        this.category = category;
    }

    /**
     * Returns the title of the training session.
     *
     * @return the training session title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the training session.
     *
     * @param title the training session title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the scheduled start date and time.
     *
     * @return the scheduled start date and time
     */
    public LocalDateTime getScheduledStart() {
        return scheduledStart;
    }

    /**
     * Sets the scheduled start date and time.
     *
     * @param scheduledStart the scheduled start date and time
     */
    public void setScheduledStart(LocalDateTime scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    /**
     * Returns the scheduled end date and time.
     *
     * @return the scheduled end date and time
     */
    public LocalDateTime getScheduledEnd() {
        return scheduledEnd;
    }

    /**
     * Sets the scheduled end date and time.
     *
     * @param scheduledEnd the scheduled end date and time
     */
    public void setScheduledEnd(LocalDateTime scheduledEnd) {
        this.scheduledEnd = scheduledEnd;
    }

    /**
     * Returns the location of the training session.
     *
     * @return the training session location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the training session.
     *
     * @param location the training session location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the maximum number of participants allowed.
     *
     * @return the training session capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the maximum number of participants allowed.
     *
     * @param capacity the training session capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Returns the current status of the training session.
     *
     * @return the training session status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the current status of the training session.
     *
     * @param status the training session status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Returns the credit awarded to the trainer.
     *
     * @return the trainer credit amount
     */
    public double getTrainerCredit() {
        return trainerCredit;
    }

    /**
     * Sets the credit awarded to the trainer.
     *
     * @param trainerCredit the trainer credit amount
     */
    public void setTrainerCredit(double trainerCredit) {
        this.trainerCredit = trainerCredit;
    }

    /**
     * Returns the name of the trainer.
     *
     * @return the trainer name
     */
    public String getTrainerName() {
        return trainerName;
    }

    /**
     * Sets the name of the trainer.
     *
     * @param trainerName the trainer name
     */
    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
}