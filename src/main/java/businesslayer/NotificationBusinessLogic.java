package businesslayer;

import java.util.List;
import dataaccesslayer.NotificationDao;
import dataaccesslayer.NotificationDaoImpl;
import transferobjects.NotificationDTO;
import transferobjects.UserDTO;

/**
 * Provides business operations for creating, retrieving, reading,
 * and archiving user notifications.
 *
 * @author Tianzhu Li
 */
public class NotificationBusinessLogic {

    private final NotificationDao notificationDao;

    /**
     * Creates the notification business logic with the default DAO
     * implementation.
     */
    public NotificationBusinessLogic() {
        this(new NotificationDaoImpl());
    }

    /**
     * Creates the notification business logic with the specified DAO.
     *
     * @param notificationDao the DAO used for notification operations
     */
    public NotificationBusinessLogic(
            NotificationDao notificationDao) {

        this.notificationDao = notificationDao;
    }

    /**
     * Validates and stores a new notification.
     *
     * @param notification the notification to create
     * @throws ValidationException if the notification is invalid
     */
    public void addNotification(
            NotificationDTO notification)
            throws ValidationException {

        if (notification == null) {
            throw new ValidationException(
                    "Notification information is required."
            );
        }

        if (notification.getUserId() == null
                || notification.getUserId() <= 0) {

            throw new ValidationException(
                    "A valid notification recipient is required."
            );
        }

        if (notification.getNotificationType() == null) {
            throw new ValidationException(
                    "Notification type is required."
            );
        }

        if (notification.getTitle() == null
                || notification.getTitle().isBlank()) {

            throw new ValidationException(
                    "Notification title is required."
            );
        }

        if (notification.getMessage() == null
                || notification.getMessage().isBlank()) {

            throw new ValidationException(
                    "Notification message is required."
            );
        }

        if (notification.getTitle().trim().length() > 150){
            throw new ValidationException(
            "Notification title cannot exceed 150 characters.");
        }

        if (notification.getMessage().trim().length() > 500){
            throw new ValidationException(
            "Notification message cannot exceed 500 characters.");
        }

        notification.setTitle(
                notification.getTitle().trim()
        );
        notification.setMessage(
                notification.getMessage().trim()
        );

        notificationDao.addNotification(notification);
    }

    /**
     * Returns all unread notifications for a user.
     *
     * @param userId the ID of the user
     * @return a list of the user's unread notifications
     * @throws ValidationException if the user ID is invalid
     */
    public List<NotificationDTO> getUnreadForUser(int userId)
            throws ValidationException {

        validateUserId(userId);

        return notificationDao.getUnreadForUser(userId);
    }

    /**
     * Returns recent notifications for users of a specified account type.
     *
     * @param userType the account type of the notification recipients
     * @param limit the maximum number of notifications to return
     * @return a list of recent notifications
     * @throws ValidationException if the user type or limit is invalid
     */
    public List<NotificationDTO> getRecentForUsersOfType(
            UserDTO.UserType userType,
            int limit) throws ValidationException {

        if (userType == null) {
            throw new ValidationException(
                    "User type is required."
            );
        }

        if (limit <= 0) {
            throw new ValidationException(
                    "Notification limit must be positive."
            );
        }

        return notificationDao.getRecentForUsersOfType(
                userType.name(), limit
        );
    }

    /**
     * Marks a notification as read for its owner.
     *
     * @param notificationId the ID of the notification
     * @param userId the ID of the notification owner
     * @return true if the notification was updated; otherwise false
     * @throws ValidationException if an identifier is invalid
     */
    public boolean markAsRead(
            int notificationId,
            int userId) throws ValidationException {

        validateNotificationId(notificationId);
        validateUserId(userId);

        return notificationDao.markAsRead(
                notificationId, userId
        );
    }

    /**
     * Archives a notification for its owner.
     *
     * @param notificationId the ID of the notification
     * @param userId the ID of the notification owner
     * @return true if the notification was updated; otherwise false
     * @throws ValidationException if an identifier is invalid
     */
    public boolean archiveNotification(
            int notificationId,
            int userId) throws ValidationException {

        validateNotificationId(notificationId);
        validateUserId(userId);

        return notificationDao.archiveNotification(
                notificationId, userId
        );
    }

    /**
     * Verifies that a user ID is positive.
     *
     * @param userId the user ID to validate
     * @throws ValidationException if the user ID is invalid
     */
    private void validateUserId(int userId)
            throws ValidationException {

        if (userId <= 0) {
            throw new ValidationException(
                    "A valid user is required."
            );
        }
    }

    /**
     * Verifies that a notification ID is positive.
     *
     * @param notificationId the notification ID to validate
     * @throws ValidationException if the notification ID is invalid
     */
    private void validateNotificationId(int notificationId)
            throws ValidationException {

        if (notificationId <= 0) {
            throw new ValidationException(
                    "A valid notification is required."
            );
        }
    }
}