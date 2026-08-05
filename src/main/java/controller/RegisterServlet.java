package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.annotation.WebServlet;

/**
 * Model 2 Controller for the Register use case. Mapped to /RegisterServlet
 * in web.xml (see WEB-INF/web.xml). Same style as LoginServlet.
 *
 * HARD-CODED for now — this only exists to prove the register.jsp form can
 * reach a servlet and the servlet can send a response back. No DAO, no
 * database, nothing is saved anywhere yet. One teammate builds the real
 * UserDao later and the "if" check below gets replaced with a call to it
 * (e.g. checking the email isn't already registered, then inserting it).
 */

//@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Browser just wants to see the registration page.
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Browser submitted the registration form.
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (name == null || name.isBlank() || email == null || email.isBlank()) {
            request.setAttribute("error", "Name and email are required.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (password == null || !password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // TEMPORARY: nothing is saved yet — no UserDao/database.
        // A teammate wires this up later, e.g.: userDao.save(...);
        System.out.println("[RegisterServlet] would save: " + name + " <" + email + ">");

        response.sendRedirect(request.getContextPath() + "/login.jsp?registered=true");
    }
}
