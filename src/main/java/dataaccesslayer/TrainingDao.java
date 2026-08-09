package dataaccesslayer;

import java.time.LocalDateTime;
import java.util.List;
import transferobjects.EquipmentDTO;
import transferobjects.TrainingBookingDTO;
import transferobjects.TrainingSessionDTO;
import transferobjects.UserQualificationDTO;

/**
 * Defines data access operations for training sessions, training bookings,
 * booking confirmations, and user qualifications.
 *
 * @author Tianzhu Li
 */
public interface TrainingDao {

    /**
     * Stores a new training session in the database.
     *
     * @param session the training session to schedule
     * @return the generated ID of the new training session
     */
    int scheduleSession(TrainingSessionDTO session);

    /**
     * Returns all training sessions assigned to a specific trainer.
     *
     * @param trainerId the ID of the trainer
     * @return a list of training sessions assigned to the trainer
     */
    List<TrainingSessionDTO> getSessionsForTrainer(int trainerId);

    /**
     * Returns all upcoming scheduled training sessions.
     *
     * @return a list of upcoming training sessions
     */
    List<TrainingSessionDTO> getUpcomingSessions();

    /**
     * Returns a training session with the specified ID.
     *
     * @param trainingSessionId the ID of the training session
     * @return the matching training session, or null if no session is found
     */
    TrainingSessionDTO getSessionById(int trainingSessionId);

    /**
     * Marks a training session as completed and records the credit awarded
     * to the trainer.
     *
     * @param trainingSessionId the ID of the completed training session
     * @param trainerCredit the credit awarded to the trainer
     */
    void completeSession(
            int trainingSessionId,
            double trainerCredit);

    /**
     * Grants or renews an equipment qualification for a user.
     *
     * @param qualification the qualification to grant
     */
    void grantQualification(
            UserQualificationDTO qualification);

    /**
     * Determines whether a user has an active, unexpired qualification for
     * an equipment category.
     *
     * @param userId the ID of the user
     * @param category the equipment category
     * @return true if the user is qualified; otherwise false
     */
    boolean isQualified(
            int userId,
            EquipmentDTO.Category category);

    /**
     * Stores a new training booking in the database.
     *
     * @param booking the training booking to create
     * @return the generated ID of the new training booking
     */
    int addBooking(TrainingBookingDTO booking);

    /**
     * Returns a training booking with the specified ID.
     *
     * @param trainingBookingId the ID of the training booking
     * @return the matching booking, or null if no booking is found
     */
    TrainingBookingDTO getBookingById(
            int trainingBookingId);

    /**
     * Returns the booking made by a trainee for a specific training
     * session.
     *
     * @param trainingSessionId the ID of the training session
     * @param traineeId the ID of the trainee
     * @return the matching booking, or null if no booking is found
     */
    TrainingBookingDTO getBookingForSessionAndTrainee(
            int trainingSessionId,
            int traineeId);

    /**
     * Returns all bookings associated with a training session.
     *
     * @param trainingSessionId the ID of the training session
     * @return a list of bookings for the training session
     */
    List<TrainingBookingDTO> getBookingsForSession(
            int trainingSessionId);

    /**
     * Returns all training bookings made by a trainee.
     *
     * @param traineeId the ID of the trainee
     * @return a list of the trainee's training bookings
     */
    List<TrainingBookingDTO> getBookingsForTrainee(
            int traineeId);

    /**
     * Counts the non-cancelled bookings for a training session.
     *
     * @param trainingSessionId the ID of the training session
     * @return the number of active bookings
     */
    int countActiveBookings(int trainingSessionId);

    /**
     * Records confirmation of a training booking by the trainee.
     *
     * @param trainingBookingId the ID of the training booking
     */
    void confirmBookingByTrainee(
            int trainingBookingId);

    /**
     * Records confirmation of a training booking by the trainer.
     *
     * @param trainingBookingId the ID of the training booking
     */
    void confirmBookingByTrainer(
            int trainingBookingId);

    /**
     * Updates the status of a training booking.
     *
     * @param trainingBookingId the ID of the training booking
     * @param status the new booking status
     */
    void updateBookingStatus(
            int trainingBookingId,
            TrainingBookingDTO.BookingStatus status);

    /**
     * Determines whether a trainer already has a scheduled or in-progress
     * session that overlaps a proposed time period.
     *
     * @param trainerId the ID of the trainer
     * @param start the proposed session start time
     * @param end the proposed session end time
     * @return true if an overlapping session exists; otherwise false
     */
    boolean hasTrainerScheduleConflict(
            int trainerId,
            LocalDateTime start,
            LocalDateTime end);
}