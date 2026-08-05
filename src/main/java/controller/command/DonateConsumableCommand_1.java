package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;

/** FR contributions: Donate Materials. */
public class DonateConsumableCommand implements Command {

    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int userId = SessionUtil.getCurrentUser(request).getUserId();
        try {
            int consumableId = Integer.parseInt(request.getParameter("consumableId"));
            double quantity = Double.parseDouble(request.getParameter("quantity"));
            consumableBL.donateMaterial(userId, consumableId, quantity);
        } catch (ValidationException | NumberFormatException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("inventoryReport", consumableBL.getInventoryReport());
        return "forward:/WEB-INF/views/consumables/consumables.jsp";
    }
}
