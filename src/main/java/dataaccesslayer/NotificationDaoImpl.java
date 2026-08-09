package dataaccesslayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transferobjects.NotificationDTO;

/**
 * Implements data access operations for user notifications.
 * Persists Observer pattern alerts, including maintenance and low-stock
 * notifications, so they remain available after the HTTP request that
 * generated them has ended.
 *
 * @author Tianzhu Li
 */
public class NotificationDaoImpl implements NotificationDao {

    /**
     * Maps the current result-set row to a notification transfer object.
     *
     * @param rs the result set containing notification data
     * @return the mapped notification
     * @throws SQLException if the result set cannot be read
     */
    private NotificationDTO map(ResultSet rs) throws SQLException {
        NotificationDTO n = new NotificationDTO();

        n.setNotificationId(rs.getInt("notification_id"));
        n.setUserId(rs.getInt("user_id"));
        n.setNotificationType(
                NotificationDTO.NotificationType.valueOf(
                        rs.getString("notification_type")
                )
        );
        n.setTitle(rs.getString("title"));
        n.setMessage(rs.getString("message"));
        n.setCreatedAt(
                rs.getTimestamp("created_at").toLocalDateTime()
        );

        Timestamp read = rs.getTimestamp("read_at");

        if (read != null) {
            n.setReadAt(read.toLocalDateTime());
        }

        n.setNotificationStatus(
                NotificationDTO.NotificationStatus.valueOf(
                        rs.getString("notification_status")
                )
        );

        return n;
    }

    /**
     * Stores a new notification in the database.
     *
     * @param n the notification to add
     */
    @Override
    public void addNotification(NotificationDTO n) {
        String sql = "INSERT INTO notifications "
                + "(user_id, notification_type, title, message) "
                + "VALUES (?,?,?,?)";

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, n.getUserId());
            ps.setString(2, n.getNotificationType().name());
            ps.setString(3, n.getTitle());
            ps.setString(4, n.getMessage());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("addNotification failed", e);
        }
    }

    /**
     * Retrieves all unread notifications for a user in reverse
     * chronological order.
     *
     * @param userId the ID of the user
     * @return a list of the user's unread notifications
     */
    @Override
    public List<NotificationDTO> getUnreadForUser(int userId) {
        String sql = "SELECT * FROM notifications "
                + "WHERE user_id = ? "
                + "AND notification_status = 'UNREAD' "
                + "ORDER BY created_at DESC";

        List<NotificationDTO> list = new ArrayList<>();

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("getUnreadForUser failed", e);
        }

        return list;
    }

    /**
     * Retrieves recent notifications for users with a specified user type.
     * Results are returned in reverse chronological order and restricted
     * to the supplied maximum number.
     *
     * @param userType the type of users whose notifications are requested
     * @param limit the maximum number of notifications to return
     * @return a list of recent notifications for the specified user type
     */
    @Override
    public List<NotificationDTO> getRecentForUsersOfType(
            String userType, int limit) {

        String sql = "SELECT n.* FROM notifications n "
                + "JOIN users u ON u.user_id = n.user_id "
                + "WHERE u.user_type = ? "
                + "ORDER BY n.created_at DESC LIMIT ?";

        List<NotificationDTO> list = new ArrayList<>();

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userType);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "getRecentForUsersOfType failed", e
            );
        }

        return list;
    }
}