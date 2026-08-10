package controller.command;

import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.MaintenanceBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import transferobjects.MaintenanceTaskDTO;

/**
 * @author Jiaying Chen
 * Schedules a maintenance task for an equipment item.
 */
public class ScheduleMaintenanceCommand implements Command {

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
            maintenanceBL.scheduleMaintenance(
                    Integer.parseInt(request.getParameter("maintenanceId")),
                    SessionUtil.getCurrentUser(request).getUserId(),
                    MaintenanceTaskDTO.MaintenanceType.valueOf(request.getParameter("maintenanceType")),
                    request.getParameter("description"),
                    MaintenanceTaskDTO.Priority.valueOf(request.getParameter("priority")),
                    LocalDateTime.parse(request.getParameter("scheduledStart")));
        } catch (ValidationException | IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/controller?action=viewMaintenanceAlerts";
    }
}
