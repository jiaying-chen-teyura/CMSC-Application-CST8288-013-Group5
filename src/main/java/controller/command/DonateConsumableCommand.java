package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;
import businesslayer.ConsumableBusinessLogic.DonationResult;
import businesslayer.ValidationException;
import controller.SessionUtil;

/**
 * @author Jiaying Chen
 * Records a consumable donation transaction.
 */
public class DonateConsumableCommand implements Command {

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
        try {
            int consumableId = Integer.parseInt(request.getParameter("consumableId"));
            double quantity = Double.parseDouble(request.getParameter("quantity"));
            DonationResult result = consumableBL.donateMaterial(userId, consumableId, quantity);
            request.setAttribute("infoMessage", "Thanks! You donated " + result.quantity + " " + result.unit
                    + " of " + result.materialName + " and earned $" + result.creditEarned + " in credits.");
        } catch (ValidationException | NumberFormatException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("inventoryReport", consumableBL.getInventoryReport());
        request.setAttribute("myDonations", consumableBL.getDonationsForUser(userId));
        return "forward:/WEB-INF/views/consumables/consumables.jsp";
    }
}
