package controlles;
import model.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> taskHistory = new ArrayList<>();
    private final int MAX_LEN_HISTORY_LIST = 10;

    @Override
    public void add(Task task){
        taskHistory.add(task);
        if (taskHistory.size() > MAX_LEN_HISTORY_LIST){
            taskHistory.remove(0);
        }
    }

    @Override
    public ArrayList<Task> getHistoryTask(){
        return taskHistory;
    }
}
