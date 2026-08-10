package controller.command;

import controller.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Jiaying Chen
 * Logs the current user out of the application.
 */
public class LogoutCommand implements Command {

    /**\n * @author Jiaying Chen
     * Executes the user logout operation.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to redirect the user to the login page
     */
    @Override
    /**
     * Executes the controller command for the current request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to forward or redirect the request
     */
    public String execute(
            HttpServletRequest request,
            HttpServletResponse response) {

        SessionUtil.clear(request);

        return "redirect:/login.jsp";
    }
}