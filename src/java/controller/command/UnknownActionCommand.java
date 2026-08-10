package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Fallback Command for an unrecognized action parameter. */
public class UnknownActionCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("errorMessage", "Unknown action requested.");
        return "forward:/WEB-INF/views/dashboard/dashboard.jsp";
    }
}
