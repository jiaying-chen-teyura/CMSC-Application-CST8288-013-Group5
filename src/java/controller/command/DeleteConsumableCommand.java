package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;
import controller.SessionUtil;
import transferobjects.UserDTO;

/** FR-04: Delete (retire) a consumable - Shop-Tech only. Soft delete so usage/donation history is preserved. */
public class DeleteConsumableCommand implements Command {

    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();

    @Override
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
