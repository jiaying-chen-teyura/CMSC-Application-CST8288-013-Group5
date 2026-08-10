package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;
import controller.SessionUtil;
import transferobjects.UserDTO;

/**
 * @author Jiaying Chen
 * Deletes an existing consumable record.
 */
public class DeleteConsumableCommand implements Command {

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
        consumableBL.deleteConsumable(Integer.parseInt(request.getParameter("consumableId")));
        return "redirect:/controller?action=viewInventory";
    }
}
