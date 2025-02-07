package controlles;

import comparartor.LocalDataTimeComparator;
import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private TreeSet<Task> taskForTime = new TreeSet<>(new LocalDataTimeComparator());
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private int nextId = 1;

    public int counterId() {
        return nextId++;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return taskForTime;
    }

    @Override
    public void add(Task task) {
        task.setId(counterId());
        taskHashMap.put(task.getId(), task);
        if (task.getStartTime() != null) {
            taskForTime.add(task);
        }
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
        updateEpicDuration(epic);
        updateEpicStartTime(epic);
        updateEpicEndTime(epic);
        if (subTask.getStartTime() != null) {
            taskForTime.add(subTask);
        }
    }

    @Override
    public void update(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
        updateEpicStatuc(epicHashMap.get(subTask.getEpicId()));
        updateEpicDuration(getEpicsById(subTask.getId()));
        updateEpicDuration(epicHashMap.get(subTask.getEpicId()));
        updateEpicStartTime(epicHashMap.get(subTask.getEpicId()));
        updateEpicEndTime(epicHashMap.get(subTask.getEpicId()));
    }

    @Override
    public void add(Epic epic) {
        epic.setId(counterId());
        epicHashMap.put(epic.getId(), epic);
        if (epic.getStartTime() != null && epic.getDuration() != null) {
            taskForTime.add(epic);
        }
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

    @Override
    public void updateEpicDuration(Epic epic) {
        Duration duration = Duration.ofMinutes(0);
        for (int indexIdSubtask : epic.getSubTaskId()){
            Duration durationSubTask = getSubTaskById(indexIdSubtask).getDuration();
            if (durationSubTask != null) {
                duration = duration.plus(durationSubTask);
            }
        }
        epic.setDuration(duration);
    }

    @Override
    public void updateEpicEndTime(Epic epic) {
        Duration duration = epic.getDuration();
        if (epic.getDuration() != null && epic.getStartTime() != null) {
            epic.setEndTime(epic.getStartTime().plus(duration));
        }
    }

    @Override
    public void updateEpicStartTime(Epic epic) {
        List<SubTask> subTaskList = subTaskHashMap.keySet().stream()
                .filter(key -> subTaskHashMap.containsKey(key))
                .map(key -> subTaskHashMap.get(key))
                .collect(Collectors.toList());
        if (subTaskList == null) {
            return;
        }
        LocalDateTime startTime = subTaskList.stream()
                        .map(subTask -> subTask.getStartTime())
                                .min(LocalDateTime ::compareTo).get();
        epic.setStartTime(startTime);
    }

    /*
    public void epicEndTime(Epic epic) {
        LocalDateTime endTime;
        Duration durationEpic = Duration.ofMinutes(0);
        if (epic.getStartTime() != null) {
            endTime = epic.getStartTime();
        } else {
            endTime = LocalDateTime.now();
        }
        for (Integer subTaskId : epic.getSubTaskId()) {
            SubTask subTask = subTaskHashMap.get(subTaskId);
            endTime = endTime.plus(subTask.getDuration());
            durationEpic = durationEpic.plus(subTask.getDuration());
        }
        epic = new Epic(epic.getTitle(), epic.getDescription(), epic.getId(), durationEpic, epic.getEndTime());
        epic.setEndTime(endTime);
    }
     */

    @Override
    public void clearAllTask() {
        deleteSubtasks();
        deleteTask();
        deleteEpic();
        taskForTime.clear();
    }

    @Override
    public void deleteTask() {
        for (Task task : taskHashMap.values()) {
            historyManager.remove(task.getId());
            taskForTime.remove(task);
        }
        taskHashMap.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epicHashMap.values()) {
            if (epic != null) {
                for (Integer indexSubTask : epic.getSubTaskId()) {
                    historyManager.remove(indexSubTask);
                    taskForTime.remove(getSubTaskById(indexSubTask));
                }
                epic.getSubTaskId().clear();
            }
            updateEpicDuration(epic);
            updateEpicStartTime(epic);
            updateEpicEndTime(epic);
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
                taskForTime.remove(epic);
            }
        }
        epicHashMap.clear();
        subTaskHashMap.clear();
    }

    @Override
    public void deleteTasks(int id) {
        if (getTaskByid(id).getStartTime() != null) {
            taskForTime.remove(getTaskByid(id));
        }
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
            updateEpicDuration(epic);
            updateEpicStartTime(epic);
            updateEpicEndTime(epic);
        }
        taskForTime.remove(getSubTaskById(id));
        historyManager.remove(id);
        subTaskHashMap.remove(id);
    }

    @Override
    public void deleteEpics(int id) {
        Epic epic = epicHashMap.get(id);
        for (Integer subTaskId : epic.getSubTaskId()) {
            taskForTime.remove(getSubTaskById(subTaskId));
            subTaskHashMap.remove(subTaskId);
            historyManager.remove(subTaskId);
        }
        if (epic.getStartTime() != null) {
            taskForTime.remove(id);
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
}
