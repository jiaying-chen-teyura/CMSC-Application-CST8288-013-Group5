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
 * @author Jiaying Chen
 * Displays equipment availability for booking.
 */
public class ViewEquipmentAvailabilityCommand implements Command {

    private final BookingBusinessLogic bookingBL = new BookingBusinessLogic();
    private final UsageSessionBusinessLogic sessionBL = new UsageSessionBusinessLogic();
    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();

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
