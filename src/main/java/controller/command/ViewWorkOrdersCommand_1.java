package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.WorkOrderBusinessLogic;
import controller.SessionUtil;
import transferobjects.UserDTO;

/** Lists work orders relevant to the current actor (open queue for Shop-Techs, own jobs for members). */
public class ViewWorkOrdersCommand implements Command {

    private final WorkOrderBusinessLogic workOrderBL = new WorkOrderBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        UserDTO user = SessionUtil.getCurrentUser(request);
        request.setAttribute("openWorkOrders", workOrderBL.getOpenWorkOrders());
        if (user.isShopTech() || user.isAdmin()) {
            request.setAttribute("myWorkOrders", workOrderBL.getWorkOrdersForShopTech(user.getUserId()));
        } else {
            request.setAttribute("myWorkOrders", workOrderBL.getWorkOrdersForMember(user.getUserId()));
        }
        return "forward:/WEB-INF/views/workorder/workorders.jsp";
    }
}
