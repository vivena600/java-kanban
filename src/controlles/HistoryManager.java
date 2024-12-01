package controlles;

import model.Task;
import java.util.ArrayList;

public interface HistoryManager {
    public void add(Task task);
    public ArrayList<Task> getHistoryTask();
}
