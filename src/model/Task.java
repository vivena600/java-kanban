package model;

import controlles.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected Duration duration;
    protected LocalDateTime startTime;
    protected String description;
    protected String title;
    protected TaskStatus status;
    protected int id;

    protected transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm");

    public Task(String title, String description, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW; //по умолчанию
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String title, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Task(String title, String description, TaskStatus status, int id,  Duration duration,
                LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
        this.duration = duration;
        this.startTime = startTime;
    }

    public TypeTask getType() {
        return TypeTask.TASK;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm");
        return "Task{" +
                "id = " + id + '\'' +
                "description= '" + description + '\'' +
                ", title = '" + title + '\'' +
                ", status = '" + status + '\'' +
                ", duration = " + duration.toMinutes() +
                ", startTime = " + (startTime != null ? startTime.format(formatter) : "null") +
                ", endTime = " + (getEndTime() != null ? getEndTime().format(formatter) : "null") +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(title, task.title) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (title != null) {
            hash += title.hashCode();
        }
        hash *= 31;

        if (description != null) {
            hash += description.hashCode();
        }
        return hash;
    }
}
