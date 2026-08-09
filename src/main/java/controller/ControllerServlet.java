package controller;

import java.io.IOException;
import java.util.Set;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.observer.InventoryAlertListener;
import businesslayer.observer.InventoryAlertService;
import businesslayer.observer.MaintenanceAlertService;
import businesslayer.observer.ShopTechAlertListener;
import controller.command.Command;
import controller.command.CommandFactory;

/**
 * Front Controller (required by the assignment): the single servlet that
 * every request in the Presentation tier goes through. It resolves the
 * "action" parameter to a Command (Command + Simple Factory patterns),
 * runs it, and dispatches to whatever view/redirect the Command returns.
 * All Business/Data tier access happens inside Commands -> businesslayer,
 * never directly from a JSP.
 */
@WebServlet(name = "ControllerServlet", urlPatterns = {"/controller"})
public class ControllerServlet extends HttpServlet {

    // Actions reachable without being logged in.
    private static final Set<String> PUBLIC_ACTIONS = Set.of("login", "register", "submitWorkOrder");

    @Override
    public void init() {
        // Wire up the Observer pattern listeners once, for the life of the application.
        MaintenanceAlertService.getInstance().addListener(new ShopTechAlertListener());
        InventoryAlertService.getInstance().addListener(new InventoryAlertListener());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        // submitWorkOrder is public for the External Client actor, but member submissions
        // (isExternal != "true") still require a session - enforced inside the Command itself.
        boolean external = "true".equals(request.getParameter("isExternal"));
        boolean requiresLogin = !(action != null && PUBLIC_ACTIONS.contains(action) && (external || !"submitWorkOrder".equals(action)));

        if (requiresLogin && !SessionUtil.isLoggedIn(request) && !"login".equals(action) && !"register".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=Please+log+in+first");
            return;
        }

        Command command = CommandFactory.getCommand(action);
        String result = command.execute(request, response);
        if (result == null || response.isCommitted()) {
            return; // command already wrote directly to the response (e.g. sendRedirect)
        }

        if (result.startsWith("redirect:")) {
            response.sendRedirect(request.getContextPath() + result.substring("redirect:".length()));
        } else if (result.startsWith("forward:")) {
            request.getRequestDispatcher(result.substring("forward:".length())).forward(request, response);
        } else {
            request.getRequestDispatcher(result).forward(request, response);
        }
    }
}
