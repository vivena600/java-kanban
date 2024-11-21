package controlles;

import model.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();

    private int nextId = 1;

    public int counterId() {
        return nextId ++;
    }

    public void add(Task task) {
        task.setId(counterId());
        taskHashMap.put(task.getId(), task);
    }

    public void update(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void add(SubTask subTask) {
        if (subTask.getId() == 0) {
            subTask.setId(counterId());
            subTaskHashMap.put(subTask.getId(), subTask);
        }

        Epic epic = epicHashMap.get(subTask.getEpicId());
        if (epic == null) {
            return;
        }
        epic.getSubTaskId().add(subTask.getId());
        updateEpicStatuc(epic);
    }

    public void update(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
        updateEpicStatuc(epicHashMap.get(subTask.getEpicId()));
    }

    public void add(Epic epic) {
        epic.setId(counterId());
        epicHashMap.put(epic.getId(), epic);
    }

    public void update(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    public void updateEpicStatuc(Epic epic) {
        int completedTasks = 0;
        int newTasks = 0;
        for (Integer subId : epic.getSubTaskId()) {
            SubTask subTask1 = subTaskHashMap.get(subId);
            if (subTask1.getStatus().equals(TaskStatus.DONE)) {
                completedTasks ++;
            } else if (subTask1.getStatus().equals(TaskStatus.NEW)) {
                newTasks ++;
            }
        }

        if (completedTasks == epic.getSubTaskId().size()) {
            epic.setStatus( TaskStatus.DONE);
        }
        else if (newTasks == epic.getSubTaskId().size() || epic.getSubTaskId().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        }
        else{
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void clearAllTask() {
        subTaskHashMap.clear();
        taskHashMap.clear();
        epicHashMap.clear();
    }

    public void deleteTasks(int id) {
        taskHashMap.remove(id);
    }
    public void deleteSubtasks(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        Epic epic = epicHashMap.get(subTask.getEpicId());

        if(epic != null) {
            epic.getSubTaskId().remove((Integer) id);
            update(epic);
        }
        subTaskHashMap.remove(id);
    }
    public void deleteEpics(int id) {
        Epic epic = epicHashMap.get(id);
        for (Integer subTaskId : epic.getSubTaskId()) {
            subTaskHashMap.remove(subTaskId);
        }
        epicHashMap.remove(id);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskHashMap.values());
    }

    public ArrayList<Task> getSubTasks() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    public ArrayList<Task> getEpics() {
        return new ArrayList<>(epicHashMap.values());
    }

    public ArrayList<SubTask> getEpicsSupTask(Epic epic) {
        ArrayList<SubTask> subtaskList = new ArrayList<SubTask>();
        for (Integer idSubTask : epic.getSubTaskId()){
            subtaskList.add(subTaskHashMap.get(idSubTask));
        }
        return  subtaskList;
    }

    public Task getTaskByid(int id) {
        return taskHashMap.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTaskHashMap.get(id);
    }

    public Epic getEpicsById(int id) {
        return epicHashMap.get(id);
    }
}
