package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.TrainingBusinessLogic;
import businesslayer.UserBusinessLogic;
import controller.SessionUtil;

/** FR-06: View Trainer Report - credits earned by training delivered, plus upcoming/past sessions. */
public class ViewTrainerReportCommand implements Command {

    private final TrainingBusinessLogic trainingBL = new TrainingBusinessLogic();
    private final UserBusinessLogic userBL = new UserBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int trainerId = SessionUtil.getCurrentUser(request).getUserId();
        request.setAttribute("mySessionsTaught", trainingBL.getSessionsForTrainer(trainerId));
        request.setAttribute("allMembers", userBL.getAllUsers());
        return "forward:/WEB-INF/views/training/training.jsp";
    }
}
