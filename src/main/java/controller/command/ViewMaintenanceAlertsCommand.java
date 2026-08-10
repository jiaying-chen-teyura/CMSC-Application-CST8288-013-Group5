package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.MaintenanceBusinessLogic;
import controller.SessionUtil;
import transferobjects.UserDTO;

/**
 * @author Jiaying Chen
 * Displays maintenance alert information.
 */
public class ViewMaintenanceAlertsCommand implements Command {

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
