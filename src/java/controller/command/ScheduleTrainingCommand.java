package controller.command;

import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.TrainingBusinessLogic;
import businesslayer.UserBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import transferobjects.EquipmentDTO;

/**
 * Trainer use case: schedule an intro/safety training session.
 * Forwards back to the Training screen with the refreshed "My Sessions"
 * list (instead of a bare redirect) so the Trainer gets immediate visible
 * feedback that the new session was created, plus a success banner.
 */
public class ScheduleTrainingCommand implements Command {

    private final TrainingBusinessLogic trainingBL = new TrainingBusinessLogic();
    private final UserBusinessLogic userBL = new UserBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int trainerId = SessionUtil.getCurrentUser(request).getUserId();
        try {
            String title = request.getParameter("title");
            trainingBL.scheduleSession(
                    trainerId,
                    EquipmentDTO.Category.valueOf(request.getParameter("category")),
                    title,
                    LocalDateTime.parse(request.getParameter("scheduledStart")),
                    LocalDateTime.parse(request.getParameter("scheduledEnd")),
                    request.getParameter("location"),
                    Integer.parseInt(request.getParameter("capacity")));
            request.setAttribute("infoMessage", "Training session \"" + title + "\" scheduled - it now appears under My Sessions below.");
        } catch (ValidationException | IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("mySessionsTaught", trainingBL.getSessionsForTrainer(trainerId));
        request.setAttribute("allMembers", userBL.getAllUsers());
        return "forward:/WEB-INF/views/training/training.jsp";
    }
}
