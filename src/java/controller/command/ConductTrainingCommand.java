package controller.command;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.TrainingBusinessLogic;
import businesslayer.ValidationException;

/** Conduct Training Session use case - marks it complete, qualifies attendees, credits the trainer. */
public class ConductTrainingCommand implements Command {

    private final TrainingBusinessLogic trainingBL = new TrainingBusinessLogic();

    @Override
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
