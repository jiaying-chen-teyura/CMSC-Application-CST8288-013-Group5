package transferobjects;

import java.time.LocalDateTime;

/**
 * Transfer object for a training session booking.
 * Stores the training session, trainee, booking status,
 * and the date and time when the booking was created.
 *
 * @author Tianzhu Li
 */
public class TrainingBookingDTO {

    /**
     * Represents the current status of a training booking.
     */
    public enum BookingStatus {
        BOOKED,
        CONFIRMED,
        ATTENDED,
        ABSENT,
        CANCELLED
    }

    private Integer trainingBookingId;
    private Integer trainingSessionId;
    private Integer traineeId;
    private BookingStatus bookingStatus = BookingStatus.BOOKED;
    private LocalDateTime bookedAt;

    /**
     * Returns the unique identifier of the training booking.
     *
     * @return the training booking ID
     */
    public Integer getTrainingBookingId() {
        return trainingBookingId;
    }

    /**
     * Sets the unique identifier of the training booking.
     *
     * @param trainingBookingId the training booking ID
     */
    public void setTrainingBookingId(Integer trainingBookingId) {
        this.trainingBookingId = trainingBookingId;
    }

    /**
     * Returns the ID of the booked training session.
     *
     * @return the training session ID
     */
    public Integer getTrainingSessionId() {
        return trainingSessionId;
    }

    /**
     * Sets the ID of the booked training session.
     *
     * @param trainingSessionId the training session ID
     */
    public void setTrainingSessionId(Integer trainingSessionId) {
        this.trainingSessionId = trainingSessionId;
    }

    /**
     * Returns the ID of the trainee who made the booking.
     *
     * @return the trainee ID
     */
    public Integer getTraineeId() {
        return traineeId;
    }

    /**
     * Sets the ID of the trainee who made the booking.
     *
     * @param traineeId the trainee ID
     */
    public void setTraineeId(Integer traineeId) {
        this.traineeId = traineeId;
    }

    /**
     * Returns the current status of the training booking.
     *
     * @return the booking status
     */
    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    /**
     * Sets the current status of the training booking.
     *
     * @param bookingStatus the booking status
     */
    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    /**
     * Returns the date and time when the booking was created.
     *
     * @return the booking creation date and time
     */
    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    /**
     * Sets the date and time when the booking was created.
     *
     * @param bookedAt the booking creation date and time
     */
    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }
}