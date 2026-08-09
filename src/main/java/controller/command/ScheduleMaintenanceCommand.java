package controller.command;

import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.MaintenanceBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import transferobjects.MaintenanceTaskDTO;

/** FR-05: Schedule Maintenance (Shop-Tech claims an ALERTED alert and picks a time). */
public class ScheduleMaintenanceCommand implements Command {

    private final MaintenanceBusinessLogic maintenanceBL = new MaintenanceBusinessLogic();

    @Override
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
