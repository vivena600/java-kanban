package controlles;

import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    private Set<Task> setTaskForTime = new TreeSet<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private int nextId = 1;

    public int counterId() {
        return nextId++;
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
                completedTasks++;
            } else if (subTask1.getStatus().equals(TaskStatus.NEW)) {
                newTasks++;
            }
        }

        if (completedTasks == epic.getSubTaskId().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else if (newTasks == epic.getSubTaskId().size() || epic.getSubTaskId().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void epicEndTime(Epic epic) {
        LocalDateTime endTime = epic.getStartTime();
        for (Integer subTaskId : epic.getSubTaskId()) {
            SubTask subTask = subTaskHashMap.get(subTaskId);
            endTime = endTime.plus(subTask.getDuration());
        }
        epic.setEndTime(endTime);
    }

    @Override
    public void clearAllTask() {
        deleteSubtasks();
        deleteTask();
        deleteEpic();
    }

    @Override
    public void deleteTask() {
        for (Task task : taskHashMap.values()) {
            historyManager.remove(task.getId());
        }
        taskHashMap.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epicHashMap.values()) {
            if (epic != null) {
                for (Integer indexSubTask : epic.getSubTaskId()) {
                    historyManager.remove(indexSubTask);
                }
                epic.getSubTaskId().clear();
            }
        }
        subTaskHashMap.clear();
    }

    @Override
    public void deleteEpic() {
        for (Epic epic : epicHashMap.values()) {
            if (epic != null) {
                for (Integer indexSubTask : epic.getSubTaskId()) {
                    historyManager.remove(indexSubTask);
                }
                epic.getSubTaskId().clear();
                historyManager.remove(epic.getId());
            }
        }
        epicHashMap.clear();
        subTaskHashMap.clear();
    }

    @Override
    public void deleteTasks(int id) {
        historyManager.remove(id);
        taskHashMap.remove(id);
    }

    @Override
    public void deleteSubtasks(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        Epic epic = epicHashMap.get(subTask.getEpicId());
        if (epic != null) {
            epic.getSubTaskId().remove((Integer) id);
            updateEpicStatuc(epic);
        }
        historyManager.remove(id);
        subTaskHashMap.remove(id);
    }

    @Override
    public void deleteEpics(int id) {
        Epic epic = epicHashMap.get(id);
        for (Integer subTaskId : epic.getSubTaskId()) {
            subTaskHashMap.remove(subTaskId);
            historyManager.remove(subTaskId);
        }
        historyManager.remove(id);
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
        ArrayList<SubTask> subtaskList = new ArrayList<>();
        for (Integer idSubTask : epic.getSubTaskId()) {
            subtaskList.add(subTaskHashMap.get(idSubTask));
        }
        return  subtaskList;
    }

    @Override
    public Task getTaskByid(int id) {
        if (taskHashMap.get(id) != null) {
            historyManager.add(taskHashMap.get(id));
        }
        return taskHashMap.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTaskHashMap.get(id) != null) {
            historyManager.add(subTaskHashMap.get(id));
        }
        return subTaskHashMap.get(id);
    }

    @Override
    public Epic getEpicsById(int id) {
        if (epicHashMap.get(id) != null) {
            historyManager.add(epicHashMap.get(id));
        }
        return epicHashMap.get(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return  new ArrayList<>(historyManager.getHistoryTask());
    }

    public Set<Task> getPrioritizedTasks() {
        return setTaskForTime;
    }
}
