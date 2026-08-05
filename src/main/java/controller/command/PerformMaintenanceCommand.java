package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.MaintenanceBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;

/** FR-05 / Shop-Tech contribution: Perform Maintenance - credits the Shop-Tech for logged time. */
public class PerformMaintenanceCommand implements Command {

    private final MaintenanceBusinessLogic maintenanceBL = new MaintenanceBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            maintenanceBL.performMaintenance(
                    Integer.parseInt(request.getParameter("maintenanceId")),
                    SessionUtil.getCurrentUser(request).getUserId(),
                    Double.parseDouble(request.getParameter("hoursSpent")));
        } catch (ValidationException | NumberFormatException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/controller?action=viewMaintenanceAlerts";
    }
}
