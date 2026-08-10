package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.*;
import controller.SessionUtil;
import transferobjects.*;

/**
 * @author Jiaying Chen
 * Loads the dashboard view for the authenticated user.
 */
public class DashboardCommand implements Command {

    private final LedgerBusinessLogic ledgerBL = new LedgerBusinessLogic();
    private final UsageSessionBusinessLogic sessionBL = new UsageSessionBusinessLogic();
    private final MaintenanceBusinessLogic maintenanceBL = new MaintenanceBusinessLogic();
    private final WorkOrderBusinessLogic workOrderBL = new WorkOrderBusinessLogic();
    private final TrainingBusinessLogic trainingBL = new TrainingBusinessLogic();
    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();

    @Override
    /**
     * Executes the controller command for the current request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to forward or redirect the request
     */
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        UserDTO user = SessionUtil.getCurrentUser(request);

        LedgerSummaryDTO summary = ledgerBL.getMonthlySummary(user.getUserId());
        request.setAttribute("ledgerSummary", summary);
        request.setAttribute("mySessions", sessionBL.getSessionsForUser(user.getUserId()));

        if (user.isShopTech()) {
            request.setAttribute("openAlerts", maintenanceBL.getOpenAlerts());
            request.setAttribute("openWorkOrders", workOrderBL.getOpenWorkOrders());
            request.setAttribute("activeSessions", sessionBL.getActiveSessions());
            request.setAttribute("inventoryReport", consumableBL.getInventoryReport());
        }
        if (user.isTrainer()) {
            request.setAttribute("mySessionsTaught", trainingBL.getSessionsForTrainer(user.getUserId()));
        }

        return "forward:/WEB-INF/views/dashboard/dashboard.jsp";
    }
}
