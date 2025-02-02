package controlles;

import model.Task;
import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);

    ArrayList<Task> getHistoryTask();

    void remove(int id);
}
