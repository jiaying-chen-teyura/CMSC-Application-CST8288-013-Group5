package controller.command;

import businesslayer.UserBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import transferobjects.UserDTO;

/**
 * Handles user login requests.
 *
 * This command authenticates the supplied email address and password.
 * When authentication succeeds, the user is stored in the HTTP session.
 */
public class LoginCommand implements Command {

    private final UserBusinessLogic userBusinessLogic
            = new UserBusinessLogic();

    /**
     * Executes the user login operation.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to redirect or forward the request
     */
    @Override
    public String execute(
            HttpServletRequest request,
            HttpServletResponse response) {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            UserDTO user = userBusinessLogic.login(
                    email,
                    password
            );

            SessionUtil.setCurrentUser(request, user);

            return "redirect:/controller?action=dashboard";

        } catch (ValidationException exception) {
            request.setAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            return "forward:/login.jsp";
        }
    }
}