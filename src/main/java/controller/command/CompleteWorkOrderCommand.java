package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ValidationException;
import businesslayer.WorkOrderBusinessLogic;
import controller.SessionUtil;

/**
 * @author Jiaying Chen
 * Completes a work order and records the earned credit.
 */
public class CompleteWorkOrderCommand implements Command {

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
        try {
            workOrderBL.completeWorkOrder(Integer.parseInt(request.getParameter("workOrderId")),
                    SessionUtil.getCurrentUser(request).getUserId());
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/controller?action=viewWorkOrders";
    }
}
