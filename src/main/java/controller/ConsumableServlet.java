package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Consumable;
import model.ConsumableDao;

/** Controller for the "Consumables" use case (donate + view stock). */
public class ConsumableServlet extends HttpServlet {

    private final ConsumableDao dao = new ConsumableDao();

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Consumable> consumables = dao.getAllConsumables();
        request.setAttribute("consumables", consumables);
        request.getRequestDispatcher("/views/consumable/consumables.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        int amount = Integer.parseInt(request.getParameter("amount"));
        dao.donate(name, amount);
        request.setAttribute("message", "Thanks for donating " + amount + " to " + name + "!");
        showList(request, response);
    }
}
