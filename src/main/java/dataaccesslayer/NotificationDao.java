package dataaccesslayer;

import java.util.List;
import transferobjects.NotificationDTO;

/**
 * Defines data access operations for user notifications.
 * Supports creating, retrieving, reading, and archiving notifications.
 *
 * @author Tianzhu Li
 */
public interface NotificationDao {

    /**
     * Stores a new notification in the database.
     *
     * @param notification the notification to add
     */
    void addNotification(NotificationDTO notification);

    /**
     * Returns all unread notifications for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of the user's unread notifications
     */
    List<NotificationDTO> getUnreadForUser(int userId);

    /**
     * Returns recent notifications for users with a specified user type.
     *
     * @param userType the type of users whose notifications are requested
     * @param limit the maximum number of notifications to return
     * @return a list of recent notifications for the specified user type
     */
    List<NotificationDTO> getRecentForUsersOfType(
            String userType, int limit);

    /**
     * Marks a notification as read and records the read time.
     * The user ID ensures that one user cannot update another user's
     * notification.
     *
     * @param notificationId the ID of the notification
     * @param userId the ID of the notification owner
     * @return true if the notification was updated; otherwise false
     */
    boolean markAsRead(int notificationId, int userId);

    /**
     * Archives a notification owned by a user.
     *
     * @param notificationId the ID of the notification
     * @param userId the ID of the notification owner
     * @return true if the notification was updated; otherwise false
     */
    boolean archiveNotification(int notificationId, int userId);
}