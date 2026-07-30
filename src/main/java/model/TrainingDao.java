package model;

import java.util.ArrayList;
import java.util.List;

public class TrainingDao {

    private static final List<TrainingSession> SESSIONS = new ArrayList<>(List.of(
        new TrainingSession("3D Printer Safety",  "Alex Kim", "2026-08-02"),
        new TrainingSession("Laser Cutter Basics", "Alex Kim", "2026-08-05")
    ));

    public List<TrainingSession> getAllSessions() {
        return SESSIONS;
    }

    public void addSession(String topic, String trainerName, String sessionDate) {
        SESSIONS.add(new TrainingSession(topic, trainerName, sessionDate));
    }
}
