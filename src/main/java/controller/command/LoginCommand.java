package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.UserBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import transferobjects.UserDTO;

/** FR-01: Login. */
public class LoginCommand implements Command {

    private final UserBusinessLogic userBL = new UserBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            UserDTO user = userBL.login(email, password);
            SessionUtil.setCurrentUser(request, user);
            return "redirect:/controller?action=dashboard";
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            return "forward:/login.jsp";
        }
    }
}
