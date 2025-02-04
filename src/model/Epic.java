package model;

import controlles.InMemoryTaskManager;
import controlles.TaskManager;
import controlles.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subTaskId = new ArrayList<>();
    protected LocalDateTime endTime;

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

    public Epic(String title, String descryption, Duration duration, LocalDateTime startTime) {
        super(title, descryption, duration, startTime);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
                ", duration = " + duration.toMinutes() +
                ", startTime = " + startTime.format(formatter) +
                ", endTime = " + getEndTime().format(formatter) +
                '}';
    }
}
