package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.UsageSessionBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;

/**
 * @author Jiaying Chen
 * Checks in equipment and finalizes a usage session.
 */
public class CheckInEquipmentCommand implements Command {

    private final UsageSessionBusinessLogic sessionBL = new UsageSessionBusinessLogic();

    @Override
    /**
     * Executes the controller command for the current request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to forward or redirect the request
     */
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
