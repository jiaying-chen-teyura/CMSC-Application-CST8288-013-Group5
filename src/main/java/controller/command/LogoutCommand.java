package controller.command;

import controller.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles user logout requests.
 *
 * This command clears the current HTTP session and redirects the user
 * to the login page.
 */
public class LogoutCommand implements Command {

    /**
     * Executes the user logout operation.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to redirect the user to the login page
     */
    @Override
    public String execute(
            HttpServletRequest request,
            HttpServletResponse response) {

        SessionUtil.clear(request);

        return "redirect:/login.jsp";
    }
}