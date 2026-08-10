package businesslayer;

import java.time.LocalDateTime;
import java.util.List;
import dataaccesslayer.*;
import transferobjects.EquipmentBookingDTO;
import transferobjects.EquipmentDTO;

/**
 * Backs the Equipment Booking use cases (View Availability, Book, Cancel) under FR-02/FR-03. 
 * @author Jiaying Chen
 */
public class BookingBusinessLogic {

    private final EquipmentBookingDao bookingDao;
    private final EquipmentDao equipmentDao;

    public BookingBusinessLogic() {
        this(new EquipmentBookingDaoImpl(), new EquipmentDaoImpl());
    }

    public BookingBusinessLogic(EquipmentBookingDao bookingDao, EquipmentDao equipmentDao) {
        this.bookingDao = bookingDao;
        this.equipmentDao = equipmentDao;
    }

    public List<EquipmentDTO> getAvailableEquipment() {
        return equipmentDao.getActiveEquipment();
    }

    public EquipmentBookingDTO bookEquipment(int userId, String assetTag, LocalDateTime start, LocalDateTime end)
            throws ValidationException {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new ValidationException("End time must be after start time.");
        }
        TimeSlotValidation.validateQuarterHourSlot(start, "Start time");
        TimeSlotValidation.validateQuarterHourSlot(end, "End time");
        if (start.isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new ValidationException("Booking start time cannot be in the past.");
        }
        EquipmentDTO equipment = equipmentDao.getEquipmentByAssetTag(assetTag);
        if (equipment == null || !equipment.isActive()) {
            throw new ValidationException("That equipment is not available for booking.");
        }
        if (!bookingDao.getOverlappingBookings(assetTag, start, end).isEmpty()) {
            throw new ValidationException("That time slot overlaps an existing booking for this equipment.");
        }

        EquipmentBookingDTO booking = new EquipmentBookingDTO();
        booking.setUserId(userId);
        booking.setAssetTag(assetTag);
        booking.setStartTime(start);
        booking.setEndTime(end);
        int id = bookingDao.addBooking(booking);
        booking.setBookingId(id);
        return booking;
    }

    public void cancelBooking(int bookingId, int requestingUserId) throws ValidationException {
        EquipmentBookingDTO booking = bookingDao.getBookingById(bookingId);
        if (booking == null) throw new ValidationException("Booking not found.");
        if (booking.getUserId() != requestingUserId) {
            throw new ValidationException("You can only cancel your own bookings.");
        }
        bookingDao.updateStatus(bookingId, EquipmentBookingDTO.BookingStatus.CANCELLED);
    }

    public List<EquipmentBookingDTO> getBookingsForUser(int userId) {
        return bookingDao.getBookingsForUser(userId);
    }

    public List<EquipmentBookingDTO> getBookingsForEquipment(String assetTag) {
        return bookingDao.getBookingsForEquipment(assetTag);
    }
}
