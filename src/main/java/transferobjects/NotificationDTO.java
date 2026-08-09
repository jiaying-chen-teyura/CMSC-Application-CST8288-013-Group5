package transferobjects;

import java.time.LocalDateTime;

/**
 * Transfer object for a user notification.
 * Stores the notification recipient, type, title, message,
 * creation time, read time, and current notification status.
 *
 * @author Tianzhu Li
 */
public class NotificationDTO {

    /**
     * Represents the type of notification sent to a user.
     */
    public enum NotificationType {
        LOW_STOCK,
        MAINTENANCE,
        TRAINING_REMINDER,
        CONFIRMATION_REQUIRED
    }

    /**
     * Represents the current status of a notification.
     */
    public enum NotificationStatus {
        UNREAD,
        READ,
        ARCHIVED
    }

    private Integer notificationId;
    private Integer userId;
    private NotificationType notificationType;
    private String title;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private NotificationStatus notificationStatus = NotificationStatus.UNREAD;

    /**
     * Returns the unique identifier of the notification.
     *
     * @return the notification ID
     */
    public Integer getNotificationId() {
        return notificationId;
    }

    /**
     * Sets the unique identifier of the notification.
     *
     * @param notificationId the notification ID
     */
    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    /**
     * Returns the ID of the user receiving the notification.
     *
     * @return the user ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user receiving the notification.
     *
     * @param userId the user ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Returns the type of the notification.
     *
     * @return the notification type
     */
    public NotificationType getNotificationType() {
        return notificationType;
    }

    /**
     * Sets the type of the notification.
     *
     * @param notificationType the notification type
     */
    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    /**
     * Returns the title of the notification.
     *
     * @return the notification title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the notification.
     *
     * @param title the notification title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the message contained in the notification.
     *
     * @return the notification message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message contained in the notification.
     *
     * @param message the notification message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the date and time when the notification was created.
     *
     * @return the notification creation date and time
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the date and time when the notification was created.
     *
     * @param createdAt the notification creation date and time
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the date and time when the notification was read.
     *
     * @return the notification read date and time
     */
    public LocalDateTime getReadAt() {
        return readAt;
    }

    /**
     * Sets the date and time when the notification was read.
     *
     * @param readAt the notification read date and time
     */
    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    /**
     * Returns the current status of the notification.
     *
     * @return the notification status
     */
    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    /**
     * Sets the current status of the notification.
     *
     * @param notificationStatus the notification status
     */
    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}