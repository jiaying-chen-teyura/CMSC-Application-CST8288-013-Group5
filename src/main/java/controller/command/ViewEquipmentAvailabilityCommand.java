package controller.command;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.BookingBusinessLogic;
import businesslayer.ConsumableBusinessLogic;
import businesslayer.UsageSessionBusinessLogic;
import controller.SessionUtil;
import transferobjects.EquipmentBookingDTO;
import transferobjects.EquipmentUsageSessionDTO;

/**
 * FR-02/03: View Equipment Availability, a member's own bookings, AND the
 * live Check In / Check Out session report - all combined onto one screen.
 * These are grouped together (instead of a separate "sessions" page)
 * because booking a slot and checking equipment in/out are two steps of
 * the same real-world action, and keeping them on one page avoids members
 * losing track of a section as the app grows.
 */
public class ViewEquipmentAvailabilityCommand implements Command {

    private final BookingBusinessLogic bookingBL = new BookingBusinessLogic();
    private final UsageSessionBusinessLogic sessionBL = new UsageSessionBusinessLogic();
    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int userId = SessionUtil.getCurrentUser(request).getUserId();
        List<EquipmentBookingDTO> myBookings = bookingBL.getBookingsForUser(userId);
        List<EquipmentUsageSessionDTO> activeSessions = sessionBL.getActiveSessions();
        sessionBL.attachActiveSessionIds(myBookings, activeSessions);

        request.setAttribute("equipmentList", bookingBL.getAvailableEquipment());
        request.setAttribute("myBookings", myBookings);
        request.setAttribute("activeSessions", activeSessions);
        request.setAttribute("consumables", consumableBL.getAllConsumables());
        return "forward:/WEB-INF/views/booking/booking.jsp";
    }
}
