package dataaccesslayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transferobjects.EquipmentDTO;
import transferobjects.TrainingSessionDTO;
import transferobjects.UserQualificationDTO;

/**
 * Implements the training data access operations.
 * Supports the trainer use cases for scheduling and conducting training
 * sessions, viewing trainer sessions, and granting user qualifications.
 *
 * @author Tianzhu Li
 */
public class TrainingDaoImpl implements TrainingDao {

    /**
     * Maps the current row of a result set to a training session transfer
     * object.
     *
     * @param rs the result set containing training session data
     * @return the mapped training session
     * @throws SQLException if the result set cannot be read
     */
    private TrainingSessionDTO map(ResultSet rs) throws SQLException {
        TrainingSessionDTO s = new TrainingSessionDTO();
        s.setTrainingSessionId(rs.getInt("training_session_id"));
        s.setTrainerId(rs.getInt("trainer_id"));
        s.setCategory(
                EquipmentDTO.Category.valueOf(rs.getString("category"))
        );
        s.setTitle(rs.getString("title"));
        s.setScheduledStart(
                rs.getTimestamp("scheduled_start").toLocalDateTime()
        );
        s.setScheduledEnd(
                rs.getTimestamp("scheduled_end").toLocalDateTime()
        );
        s.setLocation(rs.getString("location"));
        s.setCapacity(rs.getInt("capacity"));
        s.setStatus(
                TrainingSessionDTO.Status.valueOf(rs.getString("status"))
        );
        s.setTrainerCredit(rs.getDouble("trainer_credit"));

        try {
            s.setTrainerName(rs.getString("trainer_name"));
        } catch (SQLException ignored) {
        }

        return s;
    }

    /**
     * Stores a new training session and returns its generated identifier.
     *
     * @param session the training session to schedule
     * @return the generated training session ID, or -1 if no ID is returned
     */
    @Override
    public int scheduleSession(TrainingSessionDTO session) {
        String sql = "INSERT INTO training_sessions "
                + "(trainer_id, category, title, scheduled_start, "
                + "scheduled_end, location, capacity) "
                + "VALUES (?,?,?,?,?,?,?)";

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, session.getTrainerId());
            ps.setString(2, session.getCategory().name());
            ps.setString(3, session.getTitle());
            ps.setTimestamp(
                    4, Timestamp.valueOf(session.getScheduledStart())
            );
            ps.setTimestamp(
                    5, Timestamp.valueOf(session.getScheduledEnd())
            );
            ps.setString(6, session.getLocation());
            ps.setInt(7, session.getCapacity());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("scheduleSession failed", e);
        }
    }

    /**
     * Retrieves all training sessions assigned to a specific trainer.
     *
     * @param trainerId the ID of the trainer
     * @return a list of the trainer's training sessions
     */
    @Override
    public List<TrainingSessionDTO> getSessionsForTrainer(int trainerId) {
        String sql = "SELECT * FROM training_sessions "
                + "WHERE trainer_id = ? ORDER BY scheduled_start DESC";

        List<TrainingSessionDTO> list = new ArrayList<>();

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, trainerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "getSessionsForTrainer failed", e
            );
        }

        return list;
    }

    /**
     * Retrieves all scheduled training sessions in chronological order.
     * Trainer information is included with each returned session.
     *
     * @return a list of upcoming training sessions
     */
    @Override
    public List<TrainingSessionDTO> getUpcomingSessions() {
        String sql = "SELECT t.*, u.name AS trainer_name "
                + "FROM training_sessions t "
                + "JOIN users u ON u.user_id = t.trainer_id "
                + "WHERE t.status = 'SCHEDULED' "
                + "ORDER BY t.scheduled_start";

        List<TrainingSessionDTO> list = new ArrayList<>();

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("getUpcomingSessions failed", e);
        }

        return list;
    }

    /**
     * Retrieves a training session using its unique identifier.
     *
     * @param trainingSessionId the ID of the training session
     * @return the matching training session, or null if no session is found
     */
    @Override
    public TrainingSessionDTO getSessionById(int trainingSessionId) {
        String sql = "SELECT * FROM training_sessions "
                + "WHERE training_session_id = ?";

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, trainingSessionId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("getSessionById failed", e);
        }
    }

    /**
     * Marks a training session as completed and records the credit awarded
     * to the trainer.
     *
     * @param trainingSessionId the ID of the completed training session
     * @param trainerCredit the credit awarded to the trainer
     */
    @Override
    public void completeSession(
            int trainingSessionId, double trainerCredit) {

        String sql = "UPDATE training_sessions "
                + "SET status='COMPLETED', trainer_credit=? "
                + "WHERE training_session_id=?";

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, trainerCredit);
            ps.setInt(2, trainingSessionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("completeSession failed", e);
        }
    }

    /**
     * Grants or renews an equipment qualification for a user.
     * An existing qualification for the same user and category is updated
     * and reactivated.
     *
     * @param q the qualification to grant
     */
    @Override
    public void grantQualification(UserQualificationDTO q) {
        String sql = "INSERT INTO user_qualifications "
                + "(user_id, category, training_session_id, qualified_at) "
                + "VALUES (?,?,?,NOW()) "
                + "ON DUPLICATE KEY UPDATE "
                + "training_session_id=VALUES(training_session_id), "
                + "qualified_at=NOW(), qualification_status='ACTIVE'";

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, q.getUserId());
            ps.setString(2, q.getCategory().name());
            ps.setInt(3, q.getTrainingSessionId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("grantQualification failed", e);
        }
    }

    /**
     * Determines whether a user has an active qualification for an
     * equipment category.
     *
     * @param userId the ID of the user
     * @param category the equipment category
     * @return true if an active qualification exists; otherwise false
     */
    @Override
    public boolean isQualified(
            int userId, EquipmentDTO.Category category) {

        String sql = "SELECT 1 FROM user_qualifications "
                + "WHERE user_id=? AND category=? "
                + "AND qualification_status='ACTIVE'";

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, category.name());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("isQualified failed", e);
        }
    }
}