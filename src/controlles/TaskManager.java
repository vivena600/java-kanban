package controlles;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    int counterId();

    void add(Task task);

    void update(Task task);

    void add(SubTask subTask);

    void update(SubTask subTask);

    void add(Epic epic);

    void update(Epic epic);

    void updateEpicStatuc(Epic epic);

    void clearAllTask();

    void deleteTask();

    void deleteSubtasks();

    void deleteEpic();

    void deleteTasks(int id);

    void deleteSubtasks(int id);

    void deleteEpics(int id);

    ArrayList<Task> getTasks();

    ArrayList<Task> getSubTasks();

    ArrayList<Task> getEpics();

    ArrayList<SubTask> getEpicsSupTask(Epic epic);

    Task getTaskByid(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicsById(int id);
}
