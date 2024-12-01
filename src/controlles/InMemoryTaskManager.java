package controlles;

import model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private int nextId = 1;

    public int counterId() {
        return nextId ++;
    }

    @Override
    public void add(Task task) {
        task.setId(counterId());
        taskHashMap.put(task.getId(), task);
    }

    @Override
    public void update(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    @Override
    public void add(SubTask subTask) {
        if (subTask.getId() == 0) {
            subTask.setId(counterId());
        }
        subTaskHashMap.put(subTask.getId(), subTask);

        Epic epic = epicHashMap.get(subTask.getEpicId());
        if (epic == null) {
            return;
        }
        epic.getSubTaskId().add(subTask.getId());
        updateEpicStatuc(epic);
    }

    @Override
    public void update(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
        updateEpicStatuc(epicHashMap.get(subTask.getEpicId()));
    }

    @Override
    public void add(Epic epic) {
        epic.setId(counterId());
        epicHashMap.put(epic.getId(), epic);
    }

    @Override
    public void update(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    @Override
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

    @Override
    public void clearAllTask() {
        deleteSubtasks();
        deleteTask();
        deleteEpic();
    }

    @Override
    public void deleteTask(){
        taskHashMap.clear();
    }

    @Override
    public void deleteSubtasks(){
        for (Epic epic : epicHashMap.values()){
            if (epic != null){
                epic.getSubTaskId().clear();
            }
        }
        subTaskHashMap.clear();
    }

    @Override
    public void deleteEpic(){
        epicHashMap.clear();
        subTaskHashMap.clear();
    }

    @Override
    public void deleteTasks(int id) {
        taskHashMap.remove(id);
    }
    @Override
    public void deleteSubtasks(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        Epic epic = epicHashMap.get(subTask.getEpicId());

        if(epic != null) {
            epic.getSubTaskId().remove((Integer) id);
            updateEpicStatuc(epic);
        }
        subTaskHashMap.remove(id);
    }
    @Override
    public void deleteEpics(int id) {
        Epic epic = epicHashMap.get(id);
        for (Integer subTaskId : epic.getSubTaskId()) {
            subTaskHashMap.remove(subTaskId);
        }
        epicHashMap.remove(id);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskHashMap.values());
    }

    @Override
    public ArrayList<Task> getSubTasks() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    @Override
    public ArrayList<Task> getEpics() {
        return new ArrayList<>(epicHashMap.values());
    }

    @Override
    public ArrayList<SubTask> getEpicsSupTask(Epic epic) {
        ArrayList<SubTask> subtaskList = new ArrayList<SubTask>();
        for (Integer idSubTask : epic.getSubTaskId()){
            subtaskList.add(subTaskHashMap.get(idSubTask));
        }
        return  subtaskList;
    }

    @Override
    public Task getTaskByid(int id) {
        if (taskHashMap.get(id) != null){
            historyManager.add(taskHashMap.get(id));
        }
        return taskHashMap.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTaskHashMap.get(id) != null){
            historyManager.add(subTaskHashMap.get(id));
        }
        return subTaskHashMap.get(id);
    }

    @Override
    public Epic getEpicsById(int id) {
        if (epicHashMap.get(id) != null){
            historyManager.add(epicHashMap.get(id));
        }
        return epicHashMap.get(id);
    }

    @Override
    public ArrayList<Task> getHistory(){
        return  new ArrayList<>(historyManager.getHistoryTask());
    }
}
