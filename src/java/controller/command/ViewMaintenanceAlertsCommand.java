package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.MaintenanceBusinessLogic;
import controller.SessionUtil;
import transferobjects.UserDTO;

/** FR-05: View Maintenance Alerts (Shop-Tech). */
public class ViewMaintenanceAlertsCommand implements Command {

    private final MaintenanceBusinessLogic maintenanceBL = new MaintenanceBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        // Every open task (ALERTED/SCHEDULED/IN_PROGRESS), for the worklist table.
        request.setAttribute("openAlerts", maintenanceBL.getOpenAlerts());
        // Only the unclaimed ones - this is what the Schedule Maintenance dropdown offers,
        // so a Shop-Tech can only schedule equipment that actually has an open alert.
        request.setAttribute("pendingAlerts", maintenanceBL.getPendingAlerts());
        // Equipment that needs attention right now: has an open alert, or is already UNAVAILABLE.
        request.setAttribute("attentionEquipment", maintenanceBL.getEquipmentNeedingAttention());

        UserDTO user = SessionUtil.getCurrentUser(request);
        if (user != null) {
            // Feedback for the Shop-Tech: what they've actually finished, most recent first.
            request.setAttribute("myCompletedMaintenance",
                    maintenanceBL.getCompletedMaintenanceForShopTech(user.getUserId()));
        }
        return "forward:/WEB-INF/views/maintenance/maintenance.jsp";
    }
}
