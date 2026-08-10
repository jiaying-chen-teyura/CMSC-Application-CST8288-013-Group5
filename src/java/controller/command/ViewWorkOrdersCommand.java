package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.WorkOrderBusinessLogic;
import controller.SessionUtil;
import transferobjects.UserDTO;

/** Lists work orders for the Shop-Tech open queue. Restricted to Shop-Tech - Users and Trainers don't have a Work Orders section. */
public class ViewWorkOrdersCommand implements Command {

    private final WorkOrderBusinessLogic workOrderBL = new WorkOrderBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        UserDTO user = SessionUtil.getCurrentUser(request);
        if (!user.isShopTech()) {
            return "redirect:/controller?action=dashboard";
        }
        request.setAttribute("openWorkOrders", workOrderBL.getOpenWorkOrders());
        request.setAttribute("myWorkOrders", workOrderBL.getWorkOrdersForShopTech(user.getUserId()));
        return "forward:/WEB-INF/views/workorder/workorders.jsp";
    }
}
