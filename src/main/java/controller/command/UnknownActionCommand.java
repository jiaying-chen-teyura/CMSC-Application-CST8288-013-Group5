package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Jiaying Chen
 * Handles an unknown action gracefully.
 */
public class UnknownActionCommand implements Command {
    @Override
    /**
     * Executes the controller command for the current request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to forward or redirect the request
     */
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("errorMessage", "Unknown action requested.");
        return "forward:/WEB-INF/views/dashboard/dashboard.jsp";
    }
}
