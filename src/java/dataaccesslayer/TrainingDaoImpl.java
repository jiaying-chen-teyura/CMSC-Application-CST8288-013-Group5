package dataaccesslayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import transferobjects.EquipmentDTO;
import transferobjects.TrainingBookingDTO;
import transferobjects.TrainingSessionDTO;
import transferobjects.UserQualificationDTO;

/**
 * Implements data access operations for training sessions, training
 * bookings, confirmations, and user qualifications.
 *
 * @author Tianzhu Li
 */
public class TrainingDaoImpl implements TrainingDao {

    /**
     * Maps the current result-set row to a training session.
     *
     * @param rs the result set containing training session data
     * @return the mapped training session
     * @throws SQLException if the result set cannot be read
     */
    private TrainingSessionDTO map(ResultSet rs)
            throws SQLException {

        TrainingSessionDTO session =
                new TrainingSessionDTO();

        session.setTrainingSessionId(
                rs.getInt("training_session_id")
        );
        session.setTrainerId(
                rs.getInt("trainer_id")
        );
        session.setCategory(
                EquipmentDTO.Category.valueOf(
                        rs.getString("category")
                )
        );
        session.setTitle(
                rs.getString("title")
        );
        session.setScheduledStart(
                rs.getTimestamp(
                        "scheduled_start"
                ).toLocalDateTime()
        );
        session.setScheduledEnd(
                rs.getTimestamp(
                        "scheduled_end"
                ).toLocalDateTime()
        );
        session.setLocation(
                rs.getString("location")
        );
        session.setCapacity(
                rs.getInt("capacity")
        );
        session.setStatus(
                TrainingSessionDTO.Status.valueOf(
                        rs.getString("status")
                )
        );
        session.setTrainerCredit(
                rs.getDouble("trainer_credit")
        );

        try {
            session.setTrainerName(
                    rs.getString("trainer_name")
            );
        } catch (SQLException ignored) {
        }

        return session;
    }

    /**
     * Maps the current result-set row to a training booking.
     *
     * @param rs the result set containing training booking data
     * @return the mapped training booking
     * @throws SQLException if the result set cannot be read
     */
    private TrainingBookingDTO mapBooking(
            ResultSet rs) throws SQLException {

        TrainingBookingDTO booking =
                new TrainingBookingDTO();

        booking.setTrainingBookingId(
                rs.getInt("training_booking_id")
        );
        booking.setTrainingSessionId(
                rs.getInt("training_session_id")
        );
        booking.setTraineeId(
                rs.getInt("trainee_id")
        );
        booking.setBookingStatus(
                TrainingBookingDTO.BookingStatus.valueOf(
                        rs.getString("booking_status")
                )
        );
        booking.setBookedAt(
                rs.getTimestamp(
                        "booked_at"
                ).toLocalDateTime()
        );

        Timestamp traineeConfirmed =
                rs.getTimestamp(
                        "trainee_confirmed_at"
                );

        if (traineeConfirmed != null) {
            booking.setTraineeConfirmedAt(
                    traineeConfirmed.toLocalDateTime()
            );
        }

        Timestamp trainerConfirmed =
                rs.getTimestamp(
                        "trainer_confirmed_at"
                );

        if (trainerConfirmed != null) {
            booking.setTrainerConfirmedAt(
                    trainerConfirmed.toLocalDateTime()
            );
        }

        return booking;
    }

    /**
     * Stores a new training session.
     *
     * @param session the training session to schedule
     * @return the generated training session ID, or -1 if unavailable
     */
    @Override
    public int scheduleSession(
            TrainingSessionDTO session) {

        String sql = "INSERT INTO training_sessions "
                + "(trainer_id, category, title, "
                + "scheduled_start, scheduled_end, "
                + "location, capacity) "
                + "VALUES (?,?,?,?,?,?,?)";

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setInt(
                    1, session.getTrainerId()
            );
            ps.setString(
                    2, session.getCategory().name()
            );
            ps.setString(
                    3, session.getTitle()
            );
            ps.setTimestamp(
                    4,
                    Timestamp.valueOf(
                            session.getScheduledStart()
                    )
            );
            ps.setTimestamp(
                    5,
                    Timestamp.valueOf(
                            session.getScheduledEnd()
                    )
            );
            ps.setString(
                    6, session.getLocation()
            );
            ps.setInt(
                    7, session.getCapacity()
            );
            ps.executeUpdate();

            try (ResultSet keys =
                         ps.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(
                    "scheduleSession failed", e
            );
        }
    }

    /**
     * Retrieves all sessions assigned to a trainer.
     *
     * @param trainerId the ID of the trainer
     * @return a list of the trainer's sessions
     */
    @Override
    public List<TrainingSessionDTO>
            getSessionsForTrainer(int trainerId) {

        String sql = "SELECT * "
                + "FROM training_sessions "
                + "WHERE trainer_id=? "
                + "ORDER BY scheduled_start DESC";

        List<TrainingSessionDTO> sessions =
                new ArrayList<>();

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, trainerId);

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {
                    sessions.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "getSessionsForTrainer failed", e
            );
        }

        return sessions;
    }

    /**
     * Retrieves all future scheduled training sessions.
     *
     * @return a list of upcoming training sessions
     */
    @Override
    public List<TrainingSessionDTO>
            getUpcomingSessions() {

        String sql = "SELECT t.*, "
                + "u.name AS trainer_name "
                + "FROM training_sessions t "
                + "JOIN users u "
                + "ON u.user_id=t.trainer_id "
                + "WHERE t.status='SCHEDULED' "
                + "AND t.scheduled_start>"
                + "CURRENT_TIMESTAMP "
                + "ORDER BY t.scheduled_start";

        List<TrainingSessionDTO> sessions =
                new ArrayList<>();

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql);
             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {
                sessions.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "getUpcomingSessions failed", e
            );
        }

        return sessions;
    }

    /**
     * Retrieves a training session by its identifier.
     *
     * @param trainingSessionId the ID of the training session
     * @return the matching session, or null if none is found
     */
    @Override
    public TrainingSessionDTO getSessionById(
            int trainingSessionId) {

        String sql = "SELECT * "
                + "FROM training_sessions "
                + "WHERE training_session_id=?";

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, trainingSessionId);

            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next()
                        ? map(rs)
                        : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "getSessionById failed", e
            );
        }
    }

    /**
     * Marks a session as completed and records trainer credit.
     *
     * @param trainingSessionId the ID of the completed session
     * @param trainerCredit the credit awarded to the trainer
     */
    @Override
    public void completeSession(
            int trainingSessionId,
            double trainerCredit) {

        String sql = "UPDATE training_sessions "
                + "SET status='COMPLETED', "
                + "trainer_credit=? "
                + "WHERE training_session_id=?";

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setDouble(1, trainerCredit);
            ps.setInt(2, trainingSessionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "completeSession failed", e
            );
        }
    }

    /**
     * Grants or renews an equipment qualification for a user.
     *
     * @param qualification the qualification to grant
     */
    @Override
    public void grantQualification(
            UserQualificationDTO qualification) {

        String sql = "INSERT INTO "
                + "user_qualifications "
                + "(user_id, category, "
                + "training_session_id, "
                + "qualified_at) "
                + "VALUES (?,?,?,NOW()) "
                + "ON DUPLICATE KEY UPDATE "
                + "training_session_id="
                + "VALUES(training_session_id), "
                + "qualified_at=NOW(), "
                + "qualification_status='ACTIVE'";

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    qualification.getUserId()
            );
            ps.setString(
                    2,
                    qualification.getCategory().name()
            );
            ps.setInt(
                    3,
                    qualification.getTrainingSessionId()
            );
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "grantQualification failed", e
            );
        }
    }

    /**
     * Determines whether a user has an active, unexpired qualification.
     *
     * @param userId the ID of the user
     * @param category the equipment category
     * @return true if the qualification is valid; otherwise false
     */
    @Override
    public boolean isQualified(
            int userId,
            EquipmentDTO.Category category) {

        String sql = "SELECT 1 "
                + "FROM user_qualifications "
                + "WHERE user_id=? "
                + "AND category=? "
                + "AND qualification_status='ACTIVE' "
                + "AND (expires_at IS NULL "
                + "OR expires_at>NOW())";

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(
                    2, category.name()
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "isQualified failed", e
            );
        }
    }

    /**
     * Stores a new booking or reactivates a cancelled booking.
     *
     * @param booking the training booking to store
     * @return the generated or existing booking ID
     */
    @Override
    public int addBooking(
            TrainingBookingDTO booking) {

        String sql = "INSERT INTO "
                + "training_bookings "
                + "(training_session_id, trainee_id, "
                + "booking_status) "
                + "VALUES (?,?,?) "
                + "ON DUPLICATE KEY UPDATE "
                + "training_booking_id="
                + "LAST_INSERT_ID("
                + "training_booking_id), "
                + "booking_status='BOOKED', "
                + "booked_at=NOW(), "
                + "trainee_confirmed_at=NULL, "
                + "trainer_confirmed_at=NULL";

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setInt(
                    1,
                    booking.getTrainingSessionId()
            );
            ps.setInt(
                    2,
                    booking.getTraineeId()
            );
            ps.setString(
                    3,
                    booking.getBookingStatus().name()
            );
            ps.executeUpdate();

            try (ResultSet keys =
                         ps.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(
                    "addBooking failed", e
            );
        }
    }

    /**
     * Retrieves a training booking by its identifier.
     *
     * @param trainingBookingId the ID of the booking
     * @return the matching booking, or null if none is found
     */
    @Override
    public TrainingBookingDTO getBookingById(
            int trainingBookingId) {

        String sql = "SELECT * "
                + "FROM training_bookings "
                + "WHERE training_booking_id=?";

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(
                    1, trainingBookingId
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next()
                        ? mapBooking(rs)
                        : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "getBookingById failed", e
            );
        }
    }

    /**
     * Retrieves a trainee's booking for a training session.
     *
     * @param trainingSessionId the ID of the training session
     * @param traineeId the ID of the trainee
     * @return the matching booking, or null if none is found
     */
    @Override
    public TrainingBookingDTO
            getBookingForSessionAndTrainee(
                    int trainingSessionId,
                    int traineeId) {

        String sql = "SELECT * "
                + "FROM training_bookings "
                + "WHERE training_session_id=? "
                + "AND trainee_id=?";

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(
                    1, trainingSessionId
            );
            ps.setInt(
                    2, traineeId
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next()
                        ? mapBooking(rs)
                        : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "getBookingForSessionAndTrainee "
                    + "failed", e
            );
        }
    }

    /**
     * Retrieves all bookings for a training session.
     *
     * @param trainingSessionId the ID of the training session
     * @return a list of bookings for the session
     */
    @Override
    public List<TrainingBookingDTO>
            getBookingsForSession(
                    int trainingSessionId) {

        String sql = "SELECT * "
                + "FROM training_bookings "
                + "WHERE training_session_id=? "
                + "ORDER BY booked_at";

        return getBookings(
                sql,
                trainingSessionId,
                "getBookingsForSession failed"
        );
    }

    /**
     * Retrieves all bookings made by a trainee.
     *
     * @param traineeId the ID of the trainee
     * @return a list of the trainee's bookings
     */
    @Override
    public List<TrainingBookingDTO>
            getBookingsForTrainee(
                    int traineeId) {

        String sql = "SELECT * "
                + "FROM training_bookings "
                + "WHERE trainee_id=? "
                + "ORDER BY booked_at DESC";

        return getBookings(
                sql,
                traineeId,
                "getBookingsForTrainee failed"
        );
    }

    /**
     * Executes a booking query with one integer parameter.
     *
     * @param sql the booking query
     * @param id the query parameter
     * @param errorMessage the message used if the query fails
     * @return a list of matching bookings
     */
    private List<TrainingBookingDTO> getBookings(
            String sql,
            int id,
            String errorMessage) {

        List<TrainingBookingDTO> bookings =
                new ArrayList<>();

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {
                    bookings.add(
                            mapBooking(rs)
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    errorMessage, e
            );
        }

        return bookings;
    }

    /**
     * Counts non-cancelled bookings for a training session.
     *
     * @param trainingSessionId the ID of the training session
     * @return the number of active bookings
     */
    @Override
    public int countActiveBookings(
            int trainingSessionId) {

        String sql = "SELECT COUNT(*) "
                + "FROM training_bookings "
                + "WHERE training_session_id=? "
                + "AND booking_status<>'CANCELLED'";

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(
                    1, trainingSessionId
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next()
                        ? rs.getInt(1)
                        : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "countActiveBookings failed", e
            );
        }
    }

    /**
     * Records the trainee's confirmation.
     *
     * @param trainingBookingId the ID of the booking
     */
    @Override
    public void confirmBookingByTrainee(
            int trainingBookingId) {

        String sql = "UPDATE training_bookings "
                + "SET trainee_confirmed_at="
                + "COALESCE("
                + "trainee_confirmed_at,NOW()), "
                + "booking_status=CASE "
                + "WHEN trainer_confirmed_at "
                + "IS NOT NULL "
                + "THEN 'CONFIRMED' "
                + "ELSE booking_status END "
                + "WHERE training_booking_id=? "
                + "AND booking_status NOT IN "
                + "('CANCELLED','ATTENDED','ABSENT')";

        executeBookingUpdate(
                sql,
                trainingBookingId,
                "confirmBookingByTrainee failed"
        );
    }

    /**
     * Records the trainer's confirmation.
     *
     * @param trainingBookingId the ID of the booking
     */
    @Override
    public void confirmBookingByTrainer(
            int trainingBookingId) {

        String sql = "UPDATE training_bookings "
                + "SET trainer_confirmed_at="
                + "COALESCE("
                + "trainer_confirmed_at,NOW()), "
                + "booking_status=CASE "
                + "WHEN trainee_confirmed_at "
                + "IS NOT NULL "
                + "THEN 'CONFIRMED' "
                + "ELSE booking_status END "
                + "WHERE training_booking_id=? "
                + "AND booking_status NOT IN "
                + "('CANCELLED','ATTENDED','ABSENT')";

        executeBookingUpdate(
                sql,
                trainingBookingId,
                "confirmBookingByTrainer failed"
        );
    }

    /**
     * Updates the status of a training booking.
     *
     * @param trainingBookingId the ID of the booking
     * @param status the new booking status
     */
    @Override
    public void updateBookingStatus(
            int trainingBookingId,
            TrainingBookingDTO.BookingStatus status) {

        String sql = "UPDATE training_bookings "
                + "SET booking_status=? "
                + "WHERE training_booking_id=?";

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(
                    1, status.name()
            );
            ps.setInt(
                    2, trainingBookingId
            );
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "updateBookingStatus failed", e
            );
        }
    }

    /**
     * Executes a booking update using a booking ID.
     *
     * @param sql the update statement
     * @param bookingId the ID of the booking
     * @param errorMessage the message used if the update fails
     */
    private void executeBookingUpdate(
            String sql,
            int bookingId,
            String errorMessage) {

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    errorMessage, e
            );
        }
    }

    /**
     * Determines whether a trainer has an active session that overlaps a
     * proposed time period.
     *
     * @param trainerId the ID of the trainer
     * @param start the proposed session start time
     * @param end the proposed session end time
     * @return true if an overlapping session exists; otherwise false
     */
    @Override
    public boolean hasTrainerScheduleConflict(
            int trainerId,
            LocalDateTime start,
            LocalDateTime end) {

        String sql = "SELECT 1 "
                + "FROM training_sessions "
                + "WHERE trainer_id=? "
                + "AND status IN "
                + "('SCHEDULED','IN_PROGRESS') "
                + "AND scheduled_start<? "
                + "AND scheduled_end>? "
                + "LIMIT 1";

        try (Connection con =
                     DataSource.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, trainerId);
            ps.setTimestamp(
                    2,
                    Timestamp.valueOf(end)
            );
            ps.setTimestamp(
                    3,
                    Timestamp.valueOf(start)
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "hasTrainerScheduleConflict failed",
                    e
            );
        }
    }
}