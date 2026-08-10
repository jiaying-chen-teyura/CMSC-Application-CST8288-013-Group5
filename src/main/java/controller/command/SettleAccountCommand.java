package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.LedgerBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;

/**
 * @author Jiaying Chen
 * Settles account transactions for a user.
 */
public class SettleAccountCommand implements Command {

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
        try {
            ledgerBL.settleAccount(userId, Double.parseDouble(request.getParameter("amount")));
        } catch (ValidationException | NumberFormatException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/controller?action=viewLedger";
    }
}
