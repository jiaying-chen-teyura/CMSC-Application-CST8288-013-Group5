package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.MaintenanceBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;

/**
 * @author Jiaying Chen
 * Starts an existing maintenance task.
 */
public class StartMaintenanceCommand implements Command {

    private final MaintenanceBusinessLogic maintenanceBL = new MaintenanceBusinessLogic();

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
            maintenanceBL.startMaintenance(
                    Integer.parseInt(request.getParameter("maintenanceId")),
                    SessionUtil.getCurrentUser(request).getUserId());
        } catch (ValidationException | NumberFormatException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/controller?action=viewMaintenanceAlerts";
    }
}
