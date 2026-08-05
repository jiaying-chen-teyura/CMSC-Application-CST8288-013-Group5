package controller.command;

import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.TrainingBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import transferobjects.EquipmentDTO;

/** Trainer use case: schedule an intro/safety training session. */
public class ScheduleTrainingCommand implements Command {

    private final TrainingBusinessLogic trainingBL = new TrainingBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            trainingBL.scheduleSession(
                    SessionUtil.getCurrentUser(request).getUserId(),
                    EquipmentDTO.Category.valueOf(request.getParameter("category")),
                    request.getParameter("title"),
                    LocalDateTime.parse(request.getParameter("scheduledStart")),
                    LocalDateTime.parse(request.getParameter("scheduledEnd")),
                    request.getParameter("location"),
                    Integer.parseInt(request.getParameter("capacity")));
        } catch (ValidationException | IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/controller?action=viewTrainerReport";
    }
}
