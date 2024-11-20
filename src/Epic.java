import java.util.ArrayList;

public class Epic extends Task{

    ArrayList<Integer> subTaskId = new ArrayList<>();
    public Epic(String title, String description, TaskStatus status, int id) {
        super(title, description, status, id);
    }

    public Epic(String title, String descryption) {
        super(title, descryption);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskId=" + subTaskId +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
