package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.LedgerBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;

/** Settle Debits / Recharge Account use case. */
public class SettleAccountCommand implements Command {

    private final LedgerBusinessLogic ledgerBL = new LedgerBusinessLogic();

    @Override
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
