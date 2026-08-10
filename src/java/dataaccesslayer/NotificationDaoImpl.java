package dataaccesslayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transferobjects.NotificationDTO;

/**
 * Implements data access operations for user notifications.
 * Persists Observer pattern alerts and supports retrieving, reading,
 * and archiving notifications.
 *
 * @author Tianzhu Li
 */
public class NotificationDaoImpl implements NotificationDao {

    /**
     * Maps the current result-set row to a notification.
     *
     * @param rs the result set containing notification data
     * @return the mapped notification
     * @throws SQLException if the result set cannot be read
     */
    private NotificationDTO map(ResultSet rs) throws SQLException {
        NotificationDTO notification = new NotificationDTO();

        notification.setNotificationId(
                rs.getInt("notification_id")
        );
        notification.setUserId(rs.getInt("user_id"));
        notification.setNotificationType(
                NotificationDTO.NotificationType.valueOf(
                        rs.getString("notification_type")
                )
        );
        notification.setTitle(rs.getString("title"));
        notification.setMessage(rs.getString("message"));
        notification.setCreatedAt(
                rs.getTimestamp("created_at").toLocalDateTime()
        );

        Timestamp read = rs.getTimestamp("read_at");

        if (read != null) {
            notification.setReadAt(read.toLocalDateTime());
        }

        notification.setNotificationStatus(
                NotificationDTO.NotificationStatus.valueOf(
                        rs.getString("notification_status")
                )
        );

        return notification;
    }

    /**
     * Stores a new notification in the database.
     *
     * @param notification the notification to add
     */
    @Override
    public void addNotification(NotificationDTO notification) {
        String sql = "INSERT INTO notifications "
                + "(user_id, notification_type, title, message) "
                + "VALUES (?,?,?,?)";

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notification.getUserId());
            ps.setString(
                    2, notification.getNotificationType().name()
            );
            ps.setString(3, notification.getTitle());
            ps.setString(4, notification.getMessage());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("addNotification failed", e);
        }
    }

    /**
     * Retrieves all unread notifications for a user.
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
            throw new RuntimeException(
                    "getUnreadForUser failed", e
            );
        }

        return list;
    }

    /**
     * Retrieves recent notifications for users of a specified type.
     *
     * @param userType the type of users whose notifications are requested
     * @param limit the maximum number of notifications to return
     * @return a list of recent notifications
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

    /**
     * Marks a user's notification as read and records the read time.
     *
     * @param notificationId the ID of the notification
     * @param userId the ID of the notification owner
     * @return true if the notification was updated; otherwise false
     */
    @Override
    public boolean markAsRead(int notificationId, int userId) {
        String sql = "UPDATE notifications "
                + "SET notification_status='READ', "
                + "read_at=COALESCE(read_at, NOW()) "
                + "WHERE notification_id=? AND user_id=? "
                + "AND notification_status='UNREAD'";

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.setInt(2, userId);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("markAsRead failed", e);
        }
    }

    /**
     * Archives a user's notification.
     * If the notification has not previously been read, its read time is
     * also recorded.
     *
     * @param notificationId the ID of the notification
     * @param userId the ID of the notification owner
     * @return true if the notification was updated; otherwise false
     */
    @Override
    public boolean archiveNotification(
            int notificationId, int userId) {

        String sql = "UPDATE notifications "
                + "SET notification_status='ARCHIVED', "
                + "read_at=COALESCE(read_at, NOW()) "
                + "WHERE notification_id=? AND user_id=? "
                + "AND notification_status<>'ARCHIVED'";

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.setInt(2, userId);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(
                    "archiveNotification failed", e
            );
        }
    }
}