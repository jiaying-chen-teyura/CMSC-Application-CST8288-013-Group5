/* Author: Jiaying Chen */
package controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import transferobjects.UserDTO;

/**
 * Provides utility methods for managing the currently logged-in user
 * in an HTTP session.
 * @author Jiaying Chen
 */
public final class SessionUtil {

    /**
     * The session attribute used to store the currently logged-in user.
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     * Prevents the utility class from being instantiated.
     */
    private SessionUtil() {
    }

    /**
     * Retrieves the currently logged-in user from the HTTP session.
     *
     * @param request the current HTTP request
     * @return the current UserDTO, or null if no user is logged in
     */
    public static UserDTO getCurrentUser(
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        return (UserDTO) session.getAttribute(CURRENT_USER);
    }

    /**
     * Stores the currently logged-in user in the HTTP session.
     *
     * A new session is created if one does not already exist.
     *
     * @param request the current HTTP request
     * @param user the authenticated user
     */
    public static void setCurrentUser(
            HttpServletRequest request,
            UserDTO user) {

        request.getSession(true).setAttribute(
                CURRENT_USER,
                user
        );
    }

    /**
     * Invalidates the current HTTP session.
     *
     * @param request the current HTTP request
     */
    public static void clear(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * Determines whether a user is currently logged in.
     *
     * @param request the current HTTP request
     * @return true if a user is logged in; otherwise false
     */
    public static boolean isLoggedIn(
            HttpServletRequest request) {

        return getCurrentUser(request) != null;
    }
}