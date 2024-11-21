package controlles; 

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
        task.id = counterId();
        taskHashMap.put(task.id, task);
    }

    public void update(Task task) {
        taskHashMap.put(task.id, task);
    }

    public void add(SubTask subTask) {
        if (subTask.id == 0) {
            subTask.id = counterId();
            subTaskHashMap.put(subTask.id, subTask);
        }

        Epic epic = epicHashMap.get(subTask.epicId);
        if (epic == null) {
            return;
        }
        epic.subTaskId.add(subTask.id);
        updateEpicStatuc(epic);
    }

    public void update(SubTask subTask) {
        subTaskHashMap.put(subTask.id, subTask);
        updateEpicStatuc(epicHashMap.get(subTask.epicId));
    }

    public void add(Epic epic) {
        epic.id = counterId();
        epicHashMap.put(epic.id, epic);
    }

    public void update(Epic epic) {
        epicHashMap.put(epic.id, epic);
    }

    public void updateEpicStatuc(Epic epic) {
        int completedTasks = 0;
        int newTasks = 0;
        for (Integer subId : epic.subTaskId) {
            SubTask subTask1 = subTaskHashMap.get(subId);
            if (subTask1.status.equals(TaskStatus.DONE)) {
                completedTasks ++;
            } else if (subTask1.status.equals(TaskStatus.NEW)) {
                newTasks ++;
            }
        }

        if (completedTasks == epic.subTaskId.size()) {
            epic.status = TaskStatus.DONE;
        }
        else if (newTasks == epic.subTaskId.size() || epic.subTaskId.isEmpty()) {
            epic.status = TaskStatus.NEW;
        }
        else{
            epic.status = TaskStatus.IN_PROGRESS;
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
        Epic epic = epicHashMap.get(subTask.epicId);

        if(epic != null) {
            epic.subTaskId.remove((Integer) id);
            update(epic);
        }
        subTaskHashMap.remove(id);
    }
    public void deleteEpics(int id) {
        Epic epic = epicHashMap.get(id);
        for (Integer subTaskId : epic.subTaskId) {
            subTaskHashMap.remove(subTaskId);
        }
        epicHashMap.remove(id);
    }

    public ArrayList<Task> getTasks(){
        return new ArrayList<>(taskHashMap.values());
    }

    public ArrayList<Task> getSubTasks(){
        return new ArrayList<>(subTaskHashMap.values());
    }

    public ArrayList<Task> getEpics(){
        return new ArrayList<>(epicHashMap.values());
    }

    public ArrayList<SubTask> getEpicsSupTask(Epic epic){
        ArrayList<SubTask> subtaskList = new ArrayList<SubTask>();
        for (Integer idSubTask : epic.subTaskId){
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
