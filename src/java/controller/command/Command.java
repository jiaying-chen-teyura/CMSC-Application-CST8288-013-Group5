package controller.command;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Command Pattern - supports the required Front Controller. Every use case
 * bubble on the diagram maps to exactly one Command. execute() returns a
 * dispatch target: "forward:/WEB-INF/views/..." to forward to a JSP, or
 * "redirect:/controller?action=..." (or any app-relative path) to redirect.
 */
public interface Command {
    String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}
