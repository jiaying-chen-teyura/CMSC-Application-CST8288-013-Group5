package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.*;
import controller.SessionUtil;
import transferobjects.*;

/**
 * Landing page after login. Pulls a small role-specific snapshot so every
 * actor sees something relevant immediately (ledger summary for members,
 * open alerts/work orders for Shop-Techs, upcoming sessions for Trainers).
 */
public class DashboardCommand implements Command {

    private final LedgerBusinessLogic ledgerBL = new LedgerBusinessLogic();
    private final UsageSessionBusinessLogic sessionBL = new UsageSessionBusinessLogic();
    private final MaintenanceBusinessLogic maintenanceBL = new MaintenanceBusinessLogic();
    private final WorkOrderBusinessLogic workOrderBL = new WorkOrderBusinessLogic();
    private final TrainingBusinessLogic trainingBL = new TrainingBusinessLogic();
    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();

    @Override
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
