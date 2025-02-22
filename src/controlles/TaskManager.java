package controlles;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void add(Task task);

    void update(Task task);

    void add(SubTask subTask);

    void update(SubTask subTask);

    void add(Epic epic);

    void update(Epic epic);

    void updateEpicStatus(Epic epic);

    void updateLocalTimeForEpic(Epic epic);

    void updateEpicDuration(Epic epic);

    void updateEpicStartTime(Epic epic);

    void updateEpicEndTime(Epic epic);

    void clearAllTask();

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    void deleteTask(int id);

    void deleteSubtask(int id);

    void deleteEpic(int id);

    ArrayList<Task> getHistory();

    ArrayList<Task> getTasks();

    ArrayList<Task> getSubTasks();

    ArrayList<Task> getEpics();

    ArrayList<SubTask> getEpicsSupTask(Epic epic);

    Task getTaskByid(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicsById(int id);

    List<Task> getPrioritizedTasks();

    boolean validatorTime(Task newTask);
}
