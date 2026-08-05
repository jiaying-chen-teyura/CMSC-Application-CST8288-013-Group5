package controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import transferobjects.UserDTO;

/** Small helper for reading/writing the logged-in user on the HttpSession. */
public class SessionUtil {

    public static final String CURRENT_USER = "currentUser";

    private SessionUtil() { }

    public static UserDTO getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (UserDTO) session.getAttribute(CURRENT_USER);
    }

    public static void setCurrentUser(HttpServletRequest request, UserDTO user) {
        request.getSession(true).setAttribute(CURRENT_USER, user);
    }

    public static void clear(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }
}
