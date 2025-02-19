package comparartor;

import model.Task;
import  java.util.Comparator;

public class LocalDataTimeComparator implements Comparator<Task> {

    @Override
    public int compare(Task task1, Task task2) {
        return task1.getStartTime().compareTo(task2.getStartTime());
    }
}
