package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.WorkOrderBusinessLogic;
import controller.SessionUtil;
import transferobjects.UserDTO;

/**
 * @author Jiaying Chen
 * Displays work orders for the current user interface.
 */
public class ViewWorkOrdersCommand implements Command {

    private final WorkOrderBusinessLogic workOrderBL = new WorkOrderBusinessLogic();

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
            return "redirect:/controller?action=dashboard";
        }
        request.setAttribute("openWorkOrders", workOrderBL.getOpenWorkOrders());
        request.setAttribute("myWorkOrders", workOrderBL.getWorkOrdersForShopTech(user.getUserId()));
        return "forward:/WEB-INF/views/workorder/workorders.jsp";
    }
}
