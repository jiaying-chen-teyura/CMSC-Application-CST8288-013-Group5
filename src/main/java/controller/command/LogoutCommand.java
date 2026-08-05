package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import controller.SessionUtil;

/** FR-01: Logout. */
public class LogoutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        SessionUtil.clear(request);
        return "redirect:/login.jsp";
    }
}
