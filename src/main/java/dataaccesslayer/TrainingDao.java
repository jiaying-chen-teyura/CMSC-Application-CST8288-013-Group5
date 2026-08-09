package dataaccesslayer;

import java.util.List;
import transferobjects.EquipmentDTO;
import transferobjects.TrainingSessionDTO;
import transferobjects.UserQualificationDTO;

/**
 * Defines data access operations for training sessions and user
 * qualifications.
 * Provides methods for scheduling, retrieving, and completing training
 * sessions, as well as granting and verifying equipment qualifications.
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
     * Returns all upcoming training sessions.
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
    void completeSession(int trainingSessionId, double trainerCredit);

    /**
     * Grants an equipment qualification to a user.
     *
     * @param qualification the qualification to grant
     */
    void grantQualification(UserQualificationDTO qualification);

    /**
     * Determines whether a user has an active qualification for an
     * equipment category.
     *
     * @param userId the ID of the user
     * @param category the equipment category
     * @return true if the user is qualified; otherwise false
     */
    boolean isQualified(int userId, EquipmentDTO.Category category);
}