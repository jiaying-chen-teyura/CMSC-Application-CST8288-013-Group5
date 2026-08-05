package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Equipment;
import model.EquipmentDao;

/** Controller for the Shop-Tech-facing "Equipment Management" use case
 *  (register / remove equipment). Uses the SAME EquipmentDao as
 *  BookEquipmentServlet, since it's the same underlying equipment list. */
public class EquipmentManagementServlet extends HttpServlet {

    private final EquipmentDao dao = new EquipmentDao();

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Equipment> equipment = dao.getAllEquipment();
        request.setAttribute("equipment", equipment);
        request.getRequestDispatcher("/views/equipment/equipment-management.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("register".equals(action)) {
            String assetTag = request.getParameter("assetTag");
            String name = request.getParameter("name");
            String category = request.getParameter("category");
            dao.registerEquipment(assetTag, name, category);
            request.setAttribute("message", "Registered " + assetTag + ".");
        } else if ("delete".equals(action)) {
            String assetTag = request.getParameter("assetTag");
            dao.deleteEquipment(assetTag);
            request.setAttribute("message", "Removed " + assetTag + ".");
        }

        showList(request, response);
    }
}
