package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Equipment;
import model.EquipmentDao;

/** Controller for the User-facing "Book Equipment" use case. Hard-coded
 *  data via EquipmentDao for now - no database yet. */
public class BookEquipmentServlet extends HttpServlet {

    private final EquipmentDao dao = new EquipmentDao();

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Equipment> equipment = dao.getAllEquipment();
        request.setAttribute("equipment", equipment);
        request.getRequestDispatcher("/views/bookEquipment/book-equipment.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String assetTag = request.getParameter("assetTag");
        boolean booked = dao.bookEquipment(assetTag);
        request.setAttribute("message", booked
                ? "Booked " + assetTag + "."
                : "Could not book " + assetTag + " (not available).");
        showList(request, response);
    }
}
