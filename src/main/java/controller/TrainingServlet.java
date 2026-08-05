package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.TrainingSession;
import model.TrainingDao;

/** Controller for the "Training" use case. */
public class TrainingServlet extends HttpServlet {

    private final TrainingDao dao = new TrainingDao();

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<TrainingSession> sessions = dao.getAllSessions();
        request.setAttribute("sessions", sessions);
        request.getRequestDispatcher("/views/training/training.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String topic = request.getParameter("topic");
        String trainerName = request.getParameter("trainerName");
        String sessionDate = request.getParameter("sessionDate");
        dao.addSession(topic, trainerName, sessionDate);
        request.setAttribute("message", "Session \"" + topic + "\" scheduled.");
        showList(request, response);
    }
}
