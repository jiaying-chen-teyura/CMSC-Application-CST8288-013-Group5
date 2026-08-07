package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.UserBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import transferobjects.UserDTO;

/** FR-01: User Registration (User / Trainer / Shop-Tech - a Trainer/Shop-Tech is also a User). */
public class RegisterCommand implements Command {

    private final UserBusinessLogic userBL = new UserBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String name = ((firstName == null ? "" : firstName.trim()) + " " + (lastName == null ? "" : lastName.trim())).trim();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String typeParam = request.getParameter("userType");
        try {
            UserDTO.UserType type = UserDTO.UserType.valueOf(typeParam == null ? "USER" : typeParam);
            UserDTO user = userBL.register(name, email, password, type);
            SessionUtil.setCurrentUser(request, user);
            return "redirect:/controller?action=dashboard";
        } catch (ValidationException | IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            return "forward:/register.jsp";
        }
    }
}
