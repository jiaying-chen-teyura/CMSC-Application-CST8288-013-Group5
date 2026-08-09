package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.EquipmentBusinessLogic;
import businesslayer.MaintenanceBusinessLogic;

/** FR-05: View Maintenance Alerts (Shop-Tech). */
public class ViewMaintenanceAlertsCommand implements Command {

    private final MaintenanceBusinessLogic maintenanceBL = new MaintenanceBusinessLogic();
    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("openAlerts", maintenanceBL.getOpenAlerts());
        request.setAttribute("equipmentList", equipmentBL.getActiveEquipment());
        return "forward:/WEB-INF/views/maintenance/maintenance.jsp";
    }
}
