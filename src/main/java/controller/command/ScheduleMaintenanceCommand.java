package controller.command;

import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.MaintenanceBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import transferobjects.MaintenanceTaskDTO;

/** FR-05: Schedule Maintenance (Shop-Tech). */
public class ScheduleMaintenanceCommand implements Command {

    private final MaintenanceBusinessLogic maintenanceBL = new MaintenanceBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            String componentIdParam = request.getParameter("componentId");
            Integer componentId = (componentIdParam == null || componentIdParam.isBlank()) ? null : Integer.valueOf(componentIdParam);
            maintenanceBL.scheduleMaintenance(
                    request.getParameter("assetTag"),
                    componentId,
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
