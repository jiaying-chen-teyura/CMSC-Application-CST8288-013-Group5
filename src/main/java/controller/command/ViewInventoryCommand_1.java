package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;

/** FR-04: View Inventory Dashboard. */
public class ViewInventoryCommand implements Command {

    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("inventoryReport", consumableBL.getInventoryReport());
        return "forward:/WEB-INF/views/consumables/consumables.jsp";
    }
}
