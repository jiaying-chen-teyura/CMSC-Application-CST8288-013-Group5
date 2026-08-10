package dataaccesslayer;

import java.util.List;
import transferobjects.EquipmentBookingDTO;

public interface EquipmentBookingDao {
    int addBooking(EquipmentBookingDTO booking);
    EquipmentBookingDTO getBookingById(int bookingId);
    List<EquipmentBookingDTO> getBookingsForUser(int userId);
    List<EquipmentBookingDTO> getBookingsForEquipment(String assetTag);
    List<EquipmentBookingDTO> getOverlappingBookings(String assetTag, java.time.LocalDateTime start, java.time.LocalDateTime end);
    void updateStatus(int bookingId, EquipmentBookingDTO.BookingStatus status);
}
