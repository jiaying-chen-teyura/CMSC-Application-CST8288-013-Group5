package dataaccesslayer;

import java.util.List;
import transferobjects.EquipmentBookingDTO;

/**
 * Defines persistence operations for equipment booking records.
 * @author Jiaying Chen
 */
public interface EquipmentBookingDao {
    /**
     * Creates a new equipment booking and returns its generated identifier.
     *
     * @param booking the booking data to persist
     * @return the generated booking identifier, or -1 if none was generated
     */
    int addBooking(EquipmentBookingDTO booking);

    /**
     * Retrieves a booking by its unique identifier.
     *
     * @param bookingId the identifier of the booking to retrieve
     * @return the matching booking, or null if none is found
     */
    EquipmentBookingDTO getBookingById(int bookingId);

    /**
     * Retrieves all bookings created by a specific user.
     *
     * @param userId the identifier of the user whose bookings are requested
     * @return a list of matching bookings ordered by recency
     */
    List<EquipmentBookingDTO> getBookingsForUser(int userId);

    /**
     * Retrieves all bookings for a specific equipment asset.
     *
     * @param assetTag the asset tag of the equipment
     * @return a list of bookings for the equipment
     */
    List<EquipmentBookingDTO> getBookingsForEquipment(String assetTag);

    /**
     * Retrieves bookings that overlap with the supplied time window.
     *
     * @param assetTag the asset tag of the equipment
     * @param start the start of the requested time range
     * @param end the end of the requested time range
     * @return a list of overlapping bookings
     */
    List<EquipmentBookingDTO> getOverlappingBookings(String assetTag, java.time.LocalDateTime start, java.time.LocalDateTime end);

    /**
     * Updates the status of an existing booking.
     *
     * @param bookingId the identifier of the booking to update
     * @param status the new booking status
     */
    void updateStatus(int bookingId, EquipmentBookingDTO.BookingStatus status);
}
