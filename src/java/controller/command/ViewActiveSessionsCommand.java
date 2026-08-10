package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Deprecated standalone route - Check In / Out was merged into the Book
 * Equipment screen (see ViewEquipmentAvailabilityCommand) since booking and
 * checking equipment in/out are the same real-world workflow. Kept as a
 * redirect so any old bookmarked/typed links to ?action=viewActiveSessions
 * still land somewhere sensible.
 */
public class ViewActiveSessionsCommand implements Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/controller?action=viewEquipmentAvailability";
    }
}
