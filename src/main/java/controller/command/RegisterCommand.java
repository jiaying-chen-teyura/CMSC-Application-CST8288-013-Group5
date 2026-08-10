package controller.command;

import businesslayer.UserBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import transferobjects.UserDTO;

/**
 * @author Jiaying Chen
 * Handles user registration requests.
 */
public class RegisterCommand implements Command {

    private final UserBusinessLogic userBusinessLogic
            = new UserBusinessLogic();

    /**\n * @author Jiaying Chen
     * Executes the user-registration operation.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to redirect or forward the request
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

        String firstName
                = request.getParameter("firstName");

        String lastName
                = request.getParameter("lastName");

        String name = (
                (firstName == null ? "" : firstName.trim())
                + " "
                + (lastName == null ? "" : lastName.trim())
                ).trim();

        String email
                = request.getParameter("email");

        String password
                = request.getParameter("password");

        String userTypeParameter
                = request.getParameter("userType");

        try {
            UserDTO.UserType userType
                    = UserDTO.UserType.valueOf(
                            userTypeParameter == null
                                    ? "USER"
                                    : userTypeParameter
                    );

            UserDTO user = userBusinessLogic.register(
                    name,
                    email,
                    password,
                    userType
            );

            SessionUtil.setCurrentUser(request, user);

            return "redirect:/controller?action=dashboard";

        } catch (ValidationException
                | IllegalArgumentException exception) {

            request.setAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            return "forward:/register.jsp";
        }
    }
}