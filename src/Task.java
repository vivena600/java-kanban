import java.util.Objects;

public class Task {
    public String description;
    public String title;
    public TaskStatus status;
    public int id;

    public Task(String title, String description){
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW; //по умолчанию
    }

    public Task(String title, String description, TaskStatus status, int id){
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", id=" + id +
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
        if(title != null){
            hash += title.hashCode();
        }
        hash *= 31;

        if (description != null){
            hash += description.hashCode();
        }
        return hash;
    }
}
