package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;
import controller.SessionUtil;

/**
 * @author Jiaying Chen
 * Displays consumable inventory information.
 */
public class ViewInventoryCommand implements Command {

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
        request.setAttribute("inventoryReport", consumableBL.getInventoryReport());
        request.setAttribute("myDonations", consumableBL.getDonationsForUser(SessionUtil.getCurrentUser(request).getUserId()));
        return "forward:/WEB-INF/views/consumables/consumables.jsp";
    }
}
