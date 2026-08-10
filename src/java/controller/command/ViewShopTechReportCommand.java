package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.LedgerBusinessLogic;
import businesslayer.MaintenanceBusinessLogic;
import businesslayer.WorkOrderBusinessLogic;
import controller.SessionUtil;

/** FR-06: Shop-Tech report - credits earned by activity (maintenance logged, work orders completed). */
public class ViewShopTechReportCommand implements Command {

    private final LedgerBusinessLogic ledgerBL = new LedgerBusinessLogic();
    private final MaintenanceBusinessLogic maintenanceBL = new MaintenanceBusinessLogic();
    private final WorkOrderBusinessLogic workOrderBL = new WorkOrderBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int shopTechId = SessionUtil.getCurrentUser(request).getUserId();
        request.setAttribute("summary", ledgerBL.getMonthlySummary(shopTechId));
        request.setAttribute("transactions", ledgerBL.getTransactionHistory(shopTechId));
        request.setAttribute("myMaintenanceTasks", maintenanceBL.getTasksForShopTech(shopTechId));
        request.setAttribute("myWorkOrders", workOrderBL.getWorkOrdersForShopTech(shopTechId));
        return "forward:/WEB-INF/views/reports/shoptech-report.jsp";
    }
}
