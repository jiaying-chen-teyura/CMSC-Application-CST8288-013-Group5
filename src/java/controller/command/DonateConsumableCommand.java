package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;
import businesslayer.ConsumableBusinessLogic.DonationResult;
import businesslayer.ValidationException;
import controller.SessionUtil;

/** FR contributions: Donate Materials - gives the member visible feedback (credit earned + donation history) once done. */
public class DonateConsumableCommand implements Command {

    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();

    @Override
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
