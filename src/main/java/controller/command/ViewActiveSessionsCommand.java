package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Jiaying Chen
 * Displays active equipment usage sessions.
 */
public class ViewActiveSessionsCommand implements Command {

    @Override
    /**
     * Executes the controller command for the current request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to forward or redirect the request
     */
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/controller?action=viewEquipmentAvailability";
    }
}
