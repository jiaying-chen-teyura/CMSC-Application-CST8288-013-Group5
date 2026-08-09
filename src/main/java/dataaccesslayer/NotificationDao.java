package dataaccesslayer;

import java.util.List;
import transferobjects.NotificationDTO;

/**
 * Defines data access operations for user notifications.
 * Supports creating notifications and retrieving unread or recent
 * notifications for selected users.
 *
 * @author Tianzhu Li
 */
public interface NotificationDao {

    /**
     * Stores a new notification in the database.
     *
     * @param n the notification to add
     */
    void addNotification(NotificationDTO n);

    /**
     * Returns all unread notifications for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of the user's unread notifications
     */
    List<NotificationDTO> getUnreadForUser(int userId);

    /**
     * Returns recent notifications for users with a specified user type.
     * The number of returned notifications is restricted by the supplied
     * limit.
     *
     * @param userType the type of users whose notifications are requested
     * @param limit the maximum number of notifications to return
     * @return a list of recent notifications for the specified user type
     */
    List<NotificationDTO> getRecentForUsersOfType(
            String userType, int limit);
}