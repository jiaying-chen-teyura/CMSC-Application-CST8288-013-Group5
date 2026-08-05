package com.algonquin.cmsc.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.algonquin.cmsc.businesslayer.LedgerBusinessLogic;
import com.algonquin.cmsc.controller.SessionUtil;

/** FR-06: View Own Ledger Report - debits/credits for the month + amount to settle. */
public class ViewLedgerCommand implements Command {

    private final LedgerBusinessLogic ledgerBL = new LedgerBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int userId = SessionUtil.getCurrentUser(request).getUserId();
        request.setAttribute("summary", ledgerBL.getMonthlySummary(userId));
        request.setAttribute("transactions", ledgerBL.getTransactionHistory(userId));
        return "forward:/WEB-INF/views/ledger/ledger.jsp";
    }
}
