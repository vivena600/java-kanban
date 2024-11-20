public class SubTask extends Task {
    public int epicId;

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", epicid=" + epicId +
                '}';
    }

    public SubTask(String title, String description, TaskStatus status, int id, int epicId) {
        super(title, description, status, id);
        this.epicId = epicId;
        this.status = status;
    }

    public SubTask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
        this.status = TaskStatus.NEW;
    }
}
