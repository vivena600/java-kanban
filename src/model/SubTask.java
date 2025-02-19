package model;

import controlles.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String title, String description, TaskStatus status, int id, int epicId,  Duration duration,
                   LocalDateTime startTime) {
        super(title, description, status, id, duration, startTime);
        this.epicId = epicId;
        this.status = status;
    }

    public SubTask(String title, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
        this.epicId = epicId;
        this.status = TaskStatus.NEW;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", epicId=" + epicId +
                "description= '" + description + '\'' +
                ", title = '" + title + '\'' +
                ", status = '" + status + '\'' +
                ", duration = " + duration.toMinutes() +
                ", startTime = " + startTime.format(formatter) +
                ", endTime = " + getEndTime().format(formatter) +
                '}';
    }
}
