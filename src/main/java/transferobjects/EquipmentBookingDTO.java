package transferobjects;

import java.time.LocalDateTime;

/**
 * Represents an equipment booking request and its lifecycle state.
 * @author Jiaying Chen
 */
public class EquipmentBookingDTO {

    public enum BookingStatus { BOOKED, IN_PROGRESS, CANCELLED, COMPLETED, NO_SHOW }

    private Integer bookingId;
    private Integer userId;
    private String assetTag;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus bookingStatus = BookingStatus.BOOKED;
    private LocalDateTime createdAt;

    // Not persisted directly - convenience field filled in by the business
    // layer (UsageSessionBusinessLogic.attachActiveSessionIds) so the
    // Booking screen can show a Check Out button right on this booking's
    // row instead of the member having to search a separate report for it.
    private Integer activeUsageSessionId;

    /**
     * Returns the unique identifier of the booking.
     *
     * @return the booking identifier
     */
    public Integer getBookingId() { return bookingId; }

    /**
     * Sets the unique identifier of the booking.
     *
     * @param bookingId the booking identifier to assign
     */
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    /**
     * Returns the identifier of the user who made the booking.
     *
     * @return the user identifier
     */
    public Integer getUserId() { return userId; }

    /**
     * Sets the identifier of the user who made the booking.
     *
     * @param userId the user identifier to assign
     */
    public void setUserId(Integer userId) { this.userId = userId; }

    /**
     * Returns the asset tag of the equipment booked.
     *
     * @return the asset tag
     */
    public String getAssetTag() { return assetTag; }

    /**
     * Sets the asset tag of the equipment booked.
     *
     * @param assetTag the asset tag to assign
     */
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }

    /**
     * Returns the start time of the booking.
     *
     * @return the booking start time
     */
    public LocalDateTime getStartTime() { return startTime; }

    /**
     * Sets the start time of the booking.
     *
     * @param startTime the booking start time to assign
     */
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    /**
     * Returns the end time of the booking.
     *
     * @return the booking end time
     */
    public LocalDateTime getEndTime() { return endTime; }

    /**
     * Sets the end time of the booking.
     *
     * @param endTime the booking end time to assign
     */
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    /**
     * Returns the current status of the booking.
     *
     * @return the booking status
     */
    public BookingStatus getBookingStatus() { return bookingStatus; }

    /**
     * Sets the current status of the booking.
     *
     * @param bookingStatus the booking status to assign
     */
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }

    /**
     * Returns the time when the booking was created.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Sets the time when the booking was created.
     *
     * @param createdAt the creation timestamp to assign
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * Returns the active usage session identifier associated with this booking.
     *
     * @return the active usage session identifier, if any
     */
    public Integer getActiveUsageSessionId() { return activeUsageSessionId; }

    /**
     * Sets the active usage session identifier associated with this booking.
     *
     * @param activeUsageSessionId the active usage session identifier to assign
     */
    public void setActiveUsageSessionId(Integer activeUsageSessionId) { this.activeUsageSessionId = activeUsageSessionId; }
}
