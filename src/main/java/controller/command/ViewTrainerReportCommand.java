package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.TrainingBusinessLogic;
import businesslayer.UserBusinessLogic;
import controller.SessionUtil;

/**
 * @author Jiaying Chen
 * Displays the trainer report.
 */
public class ViewTrainerReportCommand implements Command {

    private final TrainingBusinessLogic trainingBL = new TrainingBusinessLogic();
    private final UserBusinessLogic userBL = new UserBusinessLogic();

    @Override
    /**
     * Executes the controller command for the current request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to forward or redirect the request
     */
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int trainerId = SessionUtil.getCurrentUser(request).getUserId();
        request.setAttribute("mySessionsTaught", trainingBL.getSessionsForTrainer(trainerId));
        request.setAttribute("allMembers", userBL.getAllUsers());
        return "forward:/WEB-INF/views/training/training.jsp";
    }
}
