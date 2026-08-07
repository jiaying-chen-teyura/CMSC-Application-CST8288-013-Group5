package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.UsageSessionBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;

/** FR-03: Check In Equipment. */
public class CheckInEquipmentCommand implements Command {

    private final UsageSessionBusinessLogic sessionBL = new UsageSessionBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int userId = SessionUtil.getCurrentUser(request).getUserId();
        String bookingIdParam = request.getParameter("bookingId");
        Integer bookingId = (bookingIdParam == null || bookingIdParam.isBlank()) ? null : Integer.valueOf(bookingIdParam);
        try {
            sessionBL.checkIn(userId, request.getParameter("assetTag"), bookingId);
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/controller?action=viewEquipmentAvailability";
    }
}
