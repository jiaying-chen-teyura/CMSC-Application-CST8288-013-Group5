package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.LedgerEntry;
import model.BillingDao;

/** Controller for the "Billing" use case. */
public class BillingServlet extends HttpServlet {

    private final BillingDao dao = new BillingDao();

    private void showLedger(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<LedgerEntry> entries = dao.getAllEntries();
        request.setAttribute("entries", entries);
        request.setAttribute("balance", dao.getBalance());
        request.getRequestDispatcher("/views/billing/billing.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showLedger(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        dao.settleDebits();
        request.setAttribute("message", "Debits settled.");
        showLedger(request, response);
    }
}
