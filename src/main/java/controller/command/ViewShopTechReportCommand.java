package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.LedgerBusinessLogic;
import businesslayer.MaintenanceBusinessLogic;
import businesslayer.WorkOrderBusinessLogic;
import controller.SessionUtil;

/**
 * @author Jiaying Chen
 * Displays the shop technician report.
 */
public class ViewShopTechReportCommand implements Command {

    private final LedgerBusinessLogic ledgerBL = new LedgerBusinessLogic();
    private final MaintenanceBusinessLogic maintenanceBL = new MaintenanceBusinessLogic();
    private final WorkOrderBusinessLogic workOrderBL = new WorkOrderBusinessLogic();

    @Override
    /**
     * Executes the controller command for the current request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to forward or redirect the request
     */
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int shopTechId = SessionUtil.getCurrentUser(request).getUserId();
        request.setAttribute("summary", ledgerBL.getMonthlySummary(shopTechId));
        request.setAttribute("transactions", ledgerBL.getTransactionHistory(shopTechId));
        request.setAttribute("myMaintenanceTasks", maintenanceBL.getTasksForShopTech(shopTechId));
        request.setAttribute("myWorkOrders", workOrderBL.getWorkOrdersForShopTech(shopTechId));
        return "forward:/WEB-INF/views/reports/shoptech-report.jsp";
    }
}
