package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
//import jakarta.servlet.annotation.WebServlet;

/**
 * Model 2 Controller for the Login use case. Mapped to /login in web.xml
 * (see WEB-INF/web.xml), same style as StudentServlet in the professor's
 * example.
 *
 * HARD-CODED for now — this only exists to prove the front-end form can
 * reach a servlet and the servlet can send a response back. No DAO, no
 * database. One teammate builds the real UserDao (plain class, same
 * shape as the professor's StudentDao) later and the two "if" checks
 * below get replaced with a call to it.
 */

//@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    
    
    
    // TEMPORARY hard-coded accounts, remove once UserDao exists.
    // One per role so the team can test what each role sees.
    //   User:      jane@abc.com   / 123
    //   Shop-Tech: tech@abc.com   / 123
    //   Trainer:   trainer@abc.com/ 123

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Browser just wants to see the login page.
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Browser submitted the login form.
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String userName = null;
        String userType = null;

        if ("jane@abc.com".equals(email) && "123".equals(password)) {
            userName = "Jane Student";
            userType = "USER";
        } else if ("tech@abc.com".equals(email) && "123".equals(password)) {
            userName = "Sam Shop-Tech";
            userType = "SHOP_TECH";
        } else if ("trainer@abc.com".equals(email) && "123".equals(password)) {
            userName = "Alex Trainer";
            userType = "TRAINER";
        }

        if (userName != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userName", userName);
            session.setAttribute("userType", userType);

            response.sendRedirect(request.getContextPath() + "/views/dashboard/dashboard.jsp");
        } else {
            request.setAttribute("error", "Invalid email or password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
