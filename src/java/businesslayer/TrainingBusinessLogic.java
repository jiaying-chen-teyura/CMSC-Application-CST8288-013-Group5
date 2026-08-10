package businesslayer;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import businesslayer.strategy.CreditContext;
import businesslayer.strategy.TrainingCreditStrategy;
import dataaccesslayer.LedgerDao;
import dataaccesslayer.LedgerDaoImpl;
import dataaccesslayer.TrainingDao;
import dataaccesslayer.TrainingDaoImpl;
import transferobjects.AccountTransactionDTO;
import transferobjects.EquipmentDTO;
import transferobjects.TrainingBookingDTO;
import transferobjects.TrainingSessionDTO;
import transferobjects.UserDTO;
import transferobjects.UserQualificationDTO;

/**
 * Provides business operations for training sessions, bookings,
 * confirmations, qualifications, and trainer credits.
 *
 * @author Tianzhu Li
 */
public class TrainingBusinessLogic {

    private final TrainingDao trainingDao;
    private final LedgerBusinessLogic ledgerBusinessLogic;
    private final UserBusinessLogic userBusinessLogic;

    /**
     * Creates the training business logic with the default
     * implementations.
     */
    public TrainingBusinessLogic() {
        this(
                new TrainingDaoImpl(),
                new LedgerDaoImpl(),
                new UserBusinessLogic()
        );
    }

    /**
     * Creates the training business logic with specified DAOs.
     *
     * @param trainingDao the DAO used for training operations
     * @param ledgerDao the DAO used for ledger operations
     */
    public TrainingBusinessLogic(
            TrainingDao trainingDao,
            LedgerDao ledgerDao) {

        this(
                trainingDao,
                ledgerDao,
                new UserBusinessLogic()
        );
    }

    /**
     * Creates the training business logic with specified dependencies.
     *
     * @param trainingDao the DAO used for training operations
     * @param ledgerDao the DAO used to create the ledger business logic
     * @param userBusinessLogic the business logic used to verify users
     */
    public TrainingBusinessLogic(
            TrainingDao trainingDao,
            LedgerDao ledgerDao,
            UserBusinessLogic userBusinessLogic) {

        this.trainingDao = trainingDao;
        this.ledgerBusinessLogic =
                new LedgerBusinessLogic(ledgerDao);
        this.userBusinessLogic = userBusinessLogic;
    }

    /**
     * Validates and schedules a new training session.
     *
     * @param trainerId the ID of the trainer
     * @param category the equipment category covered by the session
     * @param title the title of the training session
     * @param start the scheduled start date and time
     * @param end the scheduled end date and time
     * @param location the location of the training session
     * @param capacity the maximum number of trainees
     * @return the newly scheduled training session
     * @throws ValidationException if the session information is invalid
     */
    public TrainingSessionDTO scheduleSession(
            int trainerId,
            EquipmentDTO.Category category,
            String title,
            LocalDateTime start,
            LocalDateTime end,
            String location,
            int capacity) throws ValidationException {

        if (trainerId <= 0) {
            throw new ValidationException(
                    "A valid trainer is required."
            );
        }

        UserDTO trainer =
                userBusinessLogic.getById(trainerId);

        if (trainer == null
                || trainer.getUserType()
                != UserDTO.UserType.TRAINER) {

            throw new ValidationException(
                    "The selected user is not a trainer."
            );
        }

        if (trainer.getAccountStatus()
                != UserDTO.AccountStatus.ACTIVE) {

            throw new ValidationException(
                    "The trainer account is not active."
            );
        }

        if (category == null) {
            throw new ValidationException(
                    "An equipment category is required."
            );
        }

        if (title == null || title.isBlank()) {
            throw new ValidationException(
                    "Title is required."
            );
        }

        if (title.trim().length() > 150) {
            throw new ValidationException(
                    "Title cannot exceed 150 characters."
            );
        }

        if (location != null
                && location.trim().length() > 100) {

            throw new ValidationException(
                    "Location cannot exceed 100 characters."
            );
        }

        if (start == null
                || end == null
                || !end.isAfter(start)) {

            throw new ValidationException(
                    "End time must be after start time."
            );
        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new ValidationException(
                    "Start time cannot be in the past."
            );
        }

        TimeSlotValidation.validateQuarterHourSlot(
                start, "Start time"
        );

        TimeSlotValidation.validateQuarterHourSlot(
                end, "End time"
        );

        if (capacity <= 0) {
            throw new ValidationException(
                    "Capacity must be positive."
            );
        }

        if (trainingDao.hasTrainerScheduleConflict(
                trainerId, start, end)) {

            throw new ValidationException(
                    "The trainer already has a session "
                    + "during this time."
            );
        }

        TrainingSessionDTO session =
                new TrainingSessionDTO();

        session.setTrainerId(trainerId);
        session.setCategory(category);
        session.setTitle(title.trim());
        session.setScheduledStart(start);
        session.setScheduledEnd(end);
        session.setLocation(
                location == null
                        ? null
                        : location.trim()
        );
        session.setCapacity(capacity);

        int id = trainingDao.scheduleSession(session);

        if (id <= 0) {
            throw new ValidationException(
                    "The training session could not be scheduled."
            );
        }

        session.setTrainingSessionId(id);

        return session;
    }

    /**
     * Creates a booking for a trainee in an upcoming training session.
     *
     * @param trainingSessionId the ID of the training session
     * @param traineeId the ID of the trainee
     * @return the newly created or reactivated booking
     * @throws ValidationException if the session cannot be booked
     */
    public TrainingBookingDTO bookSession(
            int trainingSessionId,
            int traineeId) throws ValidationException {

        if (traineeId <= 0) {
            throw new ValidationException(
                    "A valid trainee is required."
            );
        }

        UserDTO trainee =
                userBusinessLogic.getById(traineeId);

        if (trainee == null) {
            throw new ValidationException(
                    "Trainee account not found."
            );
        }

        if (trainee.getAccountStatus()
                != UserDTO.AccountStatus.ACTIVE) {

            throw new ValidationException(
                    "The trainee account is not active."
            );
        }

        TrainingSessionDTO session =
                requireSession(trainingSessionId);

        if (session.getTrainerId().equals(traineeId)) {
            throw new ValidationException(
                    "A trainer cannot book their own "
                    + "training session."
            );
        }

        if (session.getStatus()
                != TrainingSessionDTO.Status.SCHEDULED) {

            throw new ValidationException(
                    "Only scheduled training sessions "
                    + "can be booked."
            );
        }

        if (!session.getScheduledStart()
                .isAfter(LocalDateTime.now())) {

            throw new ValidationException(
                    "This training session has already started."
            );
        }

        TrainingBookingDTO existing =
                trainingDao
                        .getBookingForSessionAndTrainee(
                                trainingSessionId,
                                traineeId
                        );

        if (existing != null
                && existing.getBookingStatus()
                != TrainingBookingDTO
                        .BookingStatus.CANCELLED) {

            throw new ValidationException(
                    "The trainee has already booked "
                    + "this session."
            );
        }

        int activeBookings =
                trainingDao.countActiveBookings(
                        trainingSessionId
                );

        if (activeBookings >= session.getCapacity()) {
            throw new ValidationException(
                    "This training session is full."
            );
        }

        TrainingBookingDTO booking =
                new TrainingBookingDTO();

        booking.setTrainingSessionId(trainingSessionId);
        booking.setTraineeId(traineeId);
        booking.setBookingStatus(
                TrainingBookingDTO.BookingStatus.BOOKED
        );

        int bookingId =
                trainingDao.addBooking(booking);

        if (bookingId <= 0) {
            throw new ValidationException(
                    "The training booking could not be created."
            );
        }

        booking.setTrainingBookingId(bookingId);

        return booking;
    }

    /**
     * Confirms a booking on behalf of the trainee who owns it.
     *
     * @param trainingBookingId the ID of the booking
     * @param traineeId the ID of the confirming trainee
     * @return the updated booking
     * @throws ValidationException if the booking cannot be confirmed
     */
    public TrainingBookingDTO confirmBookingByTrainee(
            int trainingBookingId,
            int traineeId) throws ValidationException {

        TrainingBookingDTO booking =
                requireBooking(trainingBookingId);

        if (!booking.getTraineeId().equals(traineeId)) {
            throw new ValidationException(
                    "This booking does not belong to the trainee."
            );
        }

        requireConfirmableBooking(booking);

        trainingDao.confirmBookingByTrainee(
                trainingBookingId
        );

        return trainingDao.getBookingById(
                trainingBookingId
        );
    }

    /**
     * Confirms a booking on behalf of the trainer assigned to its session.
     *
     * @param trainingBookingId the ID of the booking
     * @param trainerId the ID of the confirming trainer
     * @return the updated booking
     * @throws ValidationException if the booking cannot be confirmed
     */
    public TrainingBookingDTO confirmBookingByTrainer(
            int trainingBookingId,
            int trainerId) throws ValidationException {

        TrainingBookingDTO booking =
                requireBooking(trainingBookingId);

        TrainingSessionDTO session =
                requireSession(
                        booking.getTrainingSessionId()
                );

        if (!session.getTrainerId().equals(trainerId)) {
            throw new ValidationException(
                    "Only the assigned trainer can "
                    + "confirm this booking."
            );
        }

        requireConfirmableBooking(booking);

        trainingDao.confirmBookingByTrainer(
                trainingBookingId
        );

        return trainingDao.getBookingById(
                trainingBookingId
        );
    }

    /**
     * Cancels a training booking owned by a trainee.
     *
     * @param trainingBookingId the ID of the booking
     * @param traineeId the ID of the trainee
     * @throws ValidationException if the booking cannot be cancelled
     */
    public void cancelBooking(
            int trainingBookingId,
            int traineeId) throws ValidationException {

        TrainingBookingDTO booking =
                requireBooking(trainingBookingId);

        if (!booking.getTraineeId().equals(traineeId)) {
            throw new ValidationException(
                    "This booking does not belong to the trainee."
            );
        }

        if (booking.getBookingStatus()
                == TrainingBookingDTO.BookingStatus.ATTENDED
                || booking.getBookingStatus()
                == TrainingBookingDTO.BookingStatus.ABSENT) {

            throw new ValidationException(
                    "A completed booking cannot be cancelled."
            );
        }

        trainingDao.updateBookingStatus(
                trainingBookingId,
                TrainingBookingDTO.BookingStatus.CANCELLED
        );
    }

    /**
     * Completes a training session, records attendance, grants
     * qualifications, and records the trainer's credit.
     *
     * Existing training screens can still conduct a session without
     * booking records. When active booking records exist, attendee bookings
     * must be confirmed before attendance is recorded.
     *
     * @param trainingSessionId the ID of the training session
     * @param attendeeUserIds the IDs of attending trainees
     * @throws ValidationException if the session or attendance is invalid
     */
    public void conductSession(
            int trainingSessionId,
            List<Integer> attendeeUserIds)
            throws ValidationException {

        TrainingSessionDTO session =
                requireSession(trainingSessionId);

        if (session.getStatus()
                == TrainingSessionDTO.Status.COMPLETED) {

            throw new ValidationException(
                    "This training session has already "
                    + "been completed."
            );
        }

        if (session.getStatus()
                == TrainingSessionDTO.Status.CANCELLED) {

            throw new ValidationException(
                    "A cancelled training session "
                    + "cannot be conducted."
            );
        }

        Set<Integer> attendees =
                new LinkedHashSet<>();

        if (attendeeUserIds != null) {
            for (Integer attendeeId : attendeeUserIds) {
                if (attendeeId == null
                        || attendeeId <= 0) {

                    throw new ValidationException(
                            "The attendee list contains "
                            + "an invalid user."
                    );
                }

                attendees.add(attendeeId);
            }
        }

        List<TrainingBookingDTO> bookings =
                trainingDao.getBookingsForSession(
                        trainingSessionId
                );

        /*
         * The existing training page does not yet create booking records.
         * Strict confirmation is enabled only when the session has at
         * least one non-cancelled booking.
         */
        boolean bookingWorkflowActive = false;

        for (TrainingBookingDTO booking : bookings) {
            if (booking.getBookingStatus()
                    != TrainingBookingDTO
                            .BookingStatus.CANCELLED) {

                bookingWorkflowActive = true;
                break;
            }
        }

        if (bookingWorkflowActive) {
            for (Integer attendeeId : attendees) {
                TrainingBookingDTO booking =
                        trainingDao
                                .getBookingForSessionAndTrainee(
                                        trainingSessionId,
                                        attendeeId
                                );

                if (booking == null) {
                    throw new ValidationException(
                            "Every attendee must have "
                            + "a training booking."
                    );
                }

                if (booking.getBookingStatus()
                        != TrainingBookingDTO
                                .BookingStatus.CONFIRMED) {

                    throw new ValidationException(
                            "Every attendee booking must "
                            + "be confirmed."
                    );
                }
            }
        }

        CreditContext creditContext =
                new CreditContext(
                        new TrainingCreditStrategy()
                );

        double credit =
                creditContext.computeCredit(
                        attendees.size()
                );

        trainingDao.completeSession(
                trainingSessionId,
                credit
        );

        if (bookingWorkflowActive) {
            for (TrainingBookingDTO booking : bookings) {
                if (booking.getBookingStatus()
                        == TrainingBookingDTO
                                .BookingStatus.CANCELLED) {

                    continue;
                }

                boolean attended =
                        attendees.contains(
                                booking.getTraineeId()
                        );

                trainingDao.updateBookingStatus(
                        booking.getTrainingBookingId(),
                        attended
                                ? TrainingBookingDTO
                                        .BookingStatus.ATTENDED
                                : TrainingBookingDTO
                                        .BookingStatus.ABSENT
                );
            }
        }

        for (Integer attendeeId : attendees) {
            UserQualificationDTO qualification =
                    new UserQualificationDTO();

            qualification.setUserId(attendeeId);
            qualification.setCategory(
                    session.getCategory()
            );
            qualification.setTrainingSessionId(
                    trainingSessionId
            );

            trainingDao.grantQualification(
                    qualification
            );
        }

        AccountTransactionDTO creditTransaction =
                new AccountTransactionDTO();

        creditTransaction.setUserId(
                session.getTrainerId()
        );
        creditTransaction.setTransactionType(
                AccountTransactionDTO.TransactionType.CREDIT
        );
        creditTransaction.setActivityType(
                AccountTransactionDTO.ActivityType.TRAINING
        );
        creditTransaction.setAmount(credit);
        creditTransaction.setDescription(
                "Delivered training: "
                + session.getTitle()
        );

        ledgerBusinessLogic.recordTransaction(
                creditTransaction
        );
    }

    /**
     * Returns all sessions assigned to a trainer.
     *
     * @param trainerId the ID of the trainer
     * @return a list of the trainer's sessions
     */
    public List<TrainingSessionDTO> getSessionsForTrainer(
            int trainerId) {

        return trainingDao.getSessionsForTrainer(
                trainerId
        );
    }

    /**
     * Returns all upcoming scheduled training sessions.
     *
     * @return a list of upcoming training sessions
     */
    public List<TrainingSessionDTO> getUpcomingSessions() {
        return trainingDao.getUpcomingSessions();
    }

    /**
     * Returns all bookings for a training session.
     *
     * @param trainingSessionId the ID of the training session
     * @return a list of bookings for the session
     */
    public List<TrainingBookingDTO> getBookingsForSession(
            int trainingSessionId) {

        return trainingDao.getBookingsForSession(
                trainingSessionId
        );
    }

    /**
     * Returns all bookings made by a trainee.
     *
     * @param traineeId the ID of the trainee
     * @return a list of the trainee's bookings
     */
    public List<TrainingBookingDTO> getBookingsForTrainee(
            int traineeId) {

        return trainingDao.getBookingsForTrainee(
                traineeId
        );
    }

    /**
     * Determines whether a user has an active, unexpired qualification.
     *
     * @param userId the ID of the user
     * @param category the equipment category
     * @return true if the user is qualified; otherwise false
     */
    public boolean isQualified(
            int userId,
            EquipmentDTO.Category category) {

        return trainingDao.isQualified(
                userId,
                category
        );
    }

    /**
     * Returns a training session or throws a validation exception.
     *
     * @param trainingSessionId the ID of the session
     * @return the matching training session
     * @throws ValidationException if the session does not exist
     */
    private TrainingSessionDTO requireSession(
            int trainingSessionId)
            throws ValidationException {

        TrainingSessionDTO session =
                trainingDao.getSessionById(
                        trainingSessionId
                );

        if (session == null) {
            throw new ValidationException(
                    "Training session not found."
            );
        }

        return session;
    }

    /**
     * Returns a training booking or throws a validation exception.
     *
     * @param trainingBookingId the ID of the booking
     * @return the matching training booking
     * @throws ValidationException if the booking does not exist
     */
    private TrainingBookingDTO requireBooking(
            int trainingBookingId)
            throws ValidationException {

        TrainingBookingDTO booking =
                trainingDao.getBookingById(
                        trainingBookingId
                );

        if (booking == null) {
            throw new ValidationException(
                    "Training booking not found."
            );
        }

        return booking;
    }

    /**
     * Verifies that a booking can still be confirmed.
     *
     * @param booking the booking to verify
     * @throws ValidationException if the booking cannot be confirmed
     */
    private void requireConfirmableBooking(
            TrainingBookingDTO booking)
            throws ValidationException {

        if (booking.getBookingStatus()
                == TrainingBookingDTO.BookingStatus.CANCELLED) {

            throw new ValidationException(
                    "A cancelled booking cannot be confirmed."
            );
        }

        if (booking.getBookingStatus()
                == TrainingBookingDTO.BookingStatus.ATTENDED
                || booking.getBookingStatus()
                == TrainingBookingDTO.BookingStatus.ABSENT) {

            throw new ValidationException(
                    "A completed booking cannot be confirmed."
            );
        }

        TrainingSessionDTO session =
                requireSession(
                        booking.getTrainingSessionId()
                );

        if (session.getStatus()
                != TrainingSessionDTO.Status.SCHEDULED) {

            throw new ValidationException(
                    "Only a scheduled session "
                    + "can be confirmed."
            );
        }
    }
}