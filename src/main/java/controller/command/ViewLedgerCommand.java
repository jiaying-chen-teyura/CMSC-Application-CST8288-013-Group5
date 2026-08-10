package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.LedgerBusinessLogic;
import controller.SessionUtil;

/**
 * @author Jiaying Chen
 * Displays account ledger information.
 */
public class ViewLedgerCommand implements Command {

    private final LedgerBusinessLogic ledgerBL = new LedgerBusinessLogic();

    @Override
    /**
     * Executes the controller command for the current request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to forward or redirect the request
     */
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int userId = SessionUtil.getCurrentUser(request).getUserId();
        request.setAttribute("summary", ledgerBL.getMonthlySummary(userId));
        request.setAttribute("transactions", ledgerBL.getTransactionHistory(userId));
        return "forward:/WEB-INF/views/ledger/ledger.jsp";
    }
}
