package transferobjects;

import java.time.LocalDateTime;

public class EquipmentUsageSessionDTO {

    public enum SessionStatus { ACTIVE, COMPLETED, INTERRUPTED }

    private Integer usageSessionId;
    private Integer bookingId;
    private Integer userId;
    private String assetTag;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Integer elapsedMinutes;
    private double hourlyRate;
    private double equipmentDebit;
    private SessionStatus sessionStatus = SessionStatus.ACTIVE;

    // Not persisted directly - convenience fields filled in by the business layer for the JSPs
    private String equipmentName;
    private String userName;

    public Integer getUsageSessionId() { return usageSessionId; }
    public void setUsageSessionId(Integer usageSessionId) { this.usageSessionId = usageSessionId; }

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getAssetTag() { return assetTag; }
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }

    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }

    public Integer getElapsedMinutes() { return elapsedMinutes; }
    public void setElapsedMinutes(Integer elapsedMinutes) { this.elapsedMinutes = elapsedMinutes; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public double getEquipmentDebit() { return equipmentDebit; }
    public void setEquipmentDebit(double equipmentDebit) { this.equipmentDebit = equipmentDebit; }

    public SessionStatus getSessionStatus() { return sessionStatus; }
    public void setSessionStatus(SessionStatus sessionStatus) { this.sessionStatus = sessionStatus; }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
