package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ValidationException;
import businesslayer.WorkOrderBusinessLogic;
import businesslayer.adapter.ExternalJobRequestForm;
import controller.SessionUtil;
import transferobjects.UserDTO;
import transferobjects.WorkOrderDTO;

/**
 * Submit Work Order use case for BOTH actors on the diagram: a logged-in
 * member ("Work Orders" bubble under User), or an External Client filling
 * out the public job-request form (routed through the Adapter pattern).
 */
public class SubmitWorkOrderCommand implements Command {

    private final WorkOrderBusinessLogic workOrderBL = new WorkOrderBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        boolean external = "true".equals(request.getParameter("isExternal"));
        try {
            if (external) {
                ExternalJobRequestForm form = new ExternalJobRequestForm(
                        request.getParameter("contactName"),
                        request.getParameter("contactEmail"),
                        request.getParameter("contactPhone"),
                        request.getParameter("jobDetails"),
                        "true".equals(request.getParameter("isUrgent")));
                workOrderBL.submitExternalWorkOrder(form);
                request.setAttribute("infoMessage", "Your job request was submitted. Our team will reach out with a quote.");
                return "forward:/external-request.jsp";
            } else {
                UserDTO user = SessionUtil.getCurrentUser(request);
                double eq = parseOrZero(request.getParameter("estimatedEquipmentCost"));
                double mat = parseOrZero(request.getParameter("estimatedMaterialCost"));
                double lab = parseOrZero(request.getParameter("estimatedLabourCost"));
                WorkOrderDTO.Priority priority = "RUSH".equals(request.getParameter("priority"))
                        ? WorkOrderDTO.Priority.RUSH : WorkOrderDTO.Priority.STANDARD;
                workOrderBL.submitMemberWorkOrder(user.getUserId(), request.getParameter("description"), priority, eq, mat, lab);
                return "redirect:/controller?action=viewWorkOrders";
            }
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            return external ? "forward:/external-request.jsp"
                             : "forward:/WEB-INF/views/workorder/workorders.jsp";
        }
    }

    private double parseOrZero(String s) {
        try { return s == null || s.isBlank() ? 0 : Double.parseDouble(s); } catch (NumberFormatException e) { return 0; }
    }
}
