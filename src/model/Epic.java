package model;

import controlles.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subTaskId = new ArrayList<>();
    protected LocalDateTime endTime;

    public Epic(String title, String description, Duration duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
    }

    public Epic(String title, String description, int id, Duration duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
        this.id = id;
    }

    public Epic(String title, String description, TaskStatus status, int id, Duration duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
        this.id = id;
        this.status = status;
        this.endTime = getEndTime();
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public TypeTask getType() {
        return TypeTask.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskId=" + subTaskId +
                "id = " + id + '\'' +
                "description= '" + description + '\'' +
                ", title = '" + title + '\'' +
                ", status = '" + status + '\'' +
                ", duration = " + (duration != null ? duration.toMinutes() : "null") +
                ", startTime = " + (startTime != null ? startTime.format(formatter) : "null") +
                ", endTime = " + (endTime != null ? endTime.format(formatter) : "null") +
                '}';
    }
}
