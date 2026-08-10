package controller.command;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jiaying Chen
 * Defines the contract for controller commands in the front-controller pattern.
 */
public interface Command {
    String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}
