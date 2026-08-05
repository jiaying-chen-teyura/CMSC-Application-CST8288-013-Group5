package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.BookingBusinessLogic;
import controller.SessionUtil;

/** FR-02/03: View Equipment Availability + a member's own bookings. */
public class ViewEquipmentAvailabilityCommand implements Command {

    private final BookingBusinessLogic bookingBL = new BookingBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("equipmentList", bookingBL.getAvailableEquipment());
        request.setAttribute("myBookings", bookingBL.getBookingsForUser(SessionUtil.getCurrentUser(request).getUserId()));
        return "forward:/WEB-INF/views/booking/booking.jsp";
    }
}
