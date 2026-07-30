package model;

public class TrainingSession {
    private final String topic;
    private final String trainerName;
    private final String sessionDate;

    public TrainingSession(String topic, String trainerName, String sessionDate) {
        this.topic = topic;
        this.trainerName = trainerName;
        this.sessionDate = sessionDate;
    }

    public String getTopic() { return topic; }
    public String getTrainerName() { return trainerName; }
    public String getSessionDate() { return sessionDate; }
}
