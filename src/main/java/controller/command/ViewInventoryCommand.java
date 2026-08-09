package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;
import controller.SessionUtil;

/** FR-04: View Inventory Dashboard, plus the current member's donation history for feedback. */
public class ViewInventoryCommand implements Command {

    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("inventoryReport", consumableBL.getInventoryReport());
        request.setAttribute("myDonations", consumableBL.getDonationsForUser(SessionUtil.getCurrentUser(request).getUserId()));
        return "forward:/WEB-INF/views/consumables/consumables.jsp";
    }
}
