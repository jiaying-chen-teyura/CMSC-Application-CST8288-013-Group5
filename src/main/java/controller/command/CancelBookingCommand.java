package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.BookingBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;

/** Cancel Booking use case. */
public class CancelBookingCommand implements Command {

    private final BookingBusinessLogic bookingBL = new BookingBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int userId = SessionUtil.getCurrentUser(request).getUserId();
        try {
            bookingBL.cancelBooking(Integer.parseInt(request.getParameter("bookingId")), userId);
        } catch (ValidationException | NumberFormatException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/controller?action=viewEquipmentAvailability";
    }
}
