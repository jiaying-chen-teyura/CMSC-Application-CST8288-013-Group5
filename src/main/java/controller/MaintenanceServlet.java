package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.MaintenanceAlert;
import model.MaintenanceDao;

/** Controller for the "Predictive Maintenance" use case. */
public class MaintenanceServlet extends HttpServlet {

    private final MaintenanceDao dao = new MaintenanceDao();

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MaintenanceAlert> alerts = dao.getAllAlerts();
        request.setAttribute("alerts", alerts);
        request.getRequestDispatcher("/views/maintenance/maintenance.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String equipmentName = request.getParameter("equipmentName");
        dao.scheduleMaintenance(equipmentName);
        request.setAttribute("message", "Maintenance scheduled for " + equipmentName + ".");
        showList(request, response);
    }
}
