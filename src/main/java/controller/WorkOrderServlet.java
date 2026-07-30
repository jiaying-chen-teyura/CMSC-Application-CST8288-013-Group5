package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.WorkOrder;
import model.WorkOrderDao;

/** Controller for the "Work Orders" use case. */
public class WorkOrderServlet extends HttpServlet {

    private final WorkOrderDao dao = new WorkOrderDao();

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<WorkOrder> workOrders = dao.getAllWorkOrders();
        request.setAttribute("workOrders", workOrders);
        request.getRequestDispatcher("/views/workorder/workorders.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        dao.acceptWorkOrder(id);
        request.setAttribute("message", "Work order #" + id + " accepted.");
        showList(request, response);
    }
}
