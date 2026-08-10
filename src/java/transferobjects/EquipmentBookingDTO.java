package transferobjects;

import java.time.LocalDateTime;

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

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getAssetTag() { return assetTag; }
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getActiveUsageSessionId() { return activeUsageSessionId; }
    public void setActiveUsageSessionId(Integer activeUsageSessionId) { this.activeUsageSessionId = activeUsageSessionId; }
}
