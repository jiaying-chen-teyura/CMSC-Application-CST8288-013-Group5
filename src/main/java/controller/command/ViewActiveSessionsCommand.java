package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.BookingBusinessLogic;
import businesslayer.ConsumableBusinessLogic;
import businesslayer.EquipmentBusinessLogic;
import businesslayer.UsageSessionBusinessLogic;
import controller.SessionUtil;

/**
 * FR-03: the report listing each piece of equipment, whether it's
 * available/in-use, who's using it, and elapsed time/materials for the
 * active session. Also the screen used to trigger Check In / Check Out.
 */
public class ViewActiveSessionsCommand implements Command {

    private final UsageSessionBusinessLogic sessionBL = new UsageSessionBusinessLogic();
    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();
    private final BookingBusinessLogic bookingBL = new BookingBusinessLogic();
    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("activeSessions", sessionBL.getActiveSessions());
        request.setAttribute("equipmentList", equipmentBL.getActiveEquipment());
        request.setAttribute("consumables", consumableBL.getAllConsumables());
        request.setAttribute("myBookings", bookingBL.getBookingsForUser(SessionUtil.getCurrentUser(request).getUserId()));
        return "forward:/WEB-INF/views/booking/sessions.jsp";
    }
}
