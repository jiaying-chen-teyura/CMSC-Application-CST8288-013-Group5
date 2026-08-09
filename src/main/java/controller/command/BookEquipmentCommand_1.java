package controller.command;

import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.BookingBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;

/** Book Equipment use case. */
public class BookEquipmentCommand implements Command {

    private final BookingBusinessLogic bookingBL = new BookingBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int userId = SessionUtil.getCurrentUser(request).getUserId();
        try {
            LocalDateTime start = LocalDateTime.parse(request.getParameter("startTime"));
            LocalDateTime end = LocalDateTime.parse(request.getParameter("endTime"));
            bookingBL.bookEquipment(userId, request.getParameter("assetTag"), start, end);
        } catch (ValidationException | java.time.format.DateTimeParseException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("equipmentList", bookingBL.getAvailableEquipment());
        request.setAttribute("myBookings", bookingBL.getBookingsForUser(userId));
        return "forward:/WEB-INF/views/booking/booking.jsp";
    }
}
