package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;
import businesslayer.EquipmentBusinessLogic;
import businesslayer.MaintenanceBusinessLogic;

/** FR-06: co-op-wide equipment status list + consumable stock levels, for Shop-Tech/Admin. */
public class ViewEquipmentInventoryStatusReportCommand implements Command {

    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();
    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();
    private final MaintenanceBusinessLogic maintenanceBL = new MaintenanceBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("equipmentList", equipmentBL.getAllEquipment());
        request.setAttribute("inventoryReport", consumableBL.getInventoryReport());
        request.setAttribute("openAlerts", maintenanceBL.getOpenAlerts());
        return "forward:/WEB-INF/views/reports/equipment-inventory-report.jsp";
    }
}
