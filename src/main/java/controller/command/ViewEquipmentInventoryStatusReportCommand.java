package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;
import businesslayer.EquipmentBusinessLogic;
import businesslayer.MaintenanceBusinessLogic;

/**
 * @author Jiaying Chen
 * Displays the equipment inventory status report.
 */
public class ViewEquipmentInventoryStatusReportCommand implements Command {

    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();
    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();
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
        request.setAttribute("equipmentList", equipmentBL.getAllEquipment());
        request.setAttribute("inventoryReport", consumableBL.getInventoryReport());
        request.setAttribute("openAlerts", maintenanceBL.getOpenAlerts());
        return "forward:/WEB-INF/views/reports/equipment-inventory-report.jsp";
    }
}
