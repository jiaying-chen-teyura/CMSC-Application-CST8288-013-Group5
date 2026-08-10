package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import transferobjects.ConsumableDTO;
import transferobjects.UserDTO;

/**
 * @author Jiaying Chen
 * Registers a new consumable item.
 */
public class RegisterConsumableCommand implements Command {

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
        UserDTO user = SessionUtil.getCurrentUser(request);
        if (!user.isShopTech()) {
            request.setAttribute("errorMessage", "Only a Shop-Tech can manage consumables.");
            request.setAttribute("inventoryReport", consumableBL.getInventoryReport());
            return "forward:/WEB-INF/views/consumables/consumables.jsp";
        }
        try {
            consumableBL.registerConsumable(
                    request.getParameter("materialName"),
                    ConsumableDTO.Unit.valueOf(request.getParameter("unit")),
                    Double.parseDouble(request.getParameter("currentStock")),
                    Double.parseDouble(request.getParameter("restockLevel")),
                    Double.parseDouble(request.getParameter("unitDebitRate")));
        } catch (ValidationException | IllegalArgumentException | NullPointerException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("inventoryReport", consumableBL.getInventoryReport());
        return "forward:/WEB-INF/views/consumables/consumables.jsp";
    }
}
