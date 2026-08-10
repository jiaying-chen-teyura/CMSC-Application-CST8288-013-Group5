package controller.command;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.TrainingBusinessLogic;
import businesslayer.ValidationException;

/**
 * @author Jiaying Chen
 * Conducts a training session for the current user.
 */
public class ConductTrainingCommand implements Command {

    private final TrainingBusinessLogic trainingBL = new TrainingBusinessLogic();

    @Override
    /**
     * Executes the controller command for the current request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to forward or redirect the request
     */
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int trainingSessionId = Integer.parseInt(request.getParameter("trainingSessionId"));
        String[] attendeeParams = request.getParameterValues("attendeeUserIds");
        List<Integer> attendees = new ArrayList<>();
        if (attendeeParams != null) {
            for (String a : attendeeParams) attendees.add(Integer.valueOf(a));
        }
        try {
            trainingBL.conductSession(trainingSessionId, attendees);
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/controller?action=viewTrainerReport";
    }
}
