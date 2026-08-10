package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ValidationException;
import businesslayer.WorkOrderBusinessLogic;
import controller.SessionUtil;

/** Complete Work Order use case (Shop-Tech) - credits the Shop-Tech, debits the member if applicable. */
public class CompleteWorkOrderCommand implements Command {

    private final WorkOrderBusinessLogic workOrderBL = new WorkOrderBusinessLogic();

    @Override
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
