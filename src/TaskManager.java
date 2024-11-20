import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicHashMap = new HashMap<>();

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
        if (epic == null) { // Если Epic не найден, просто завершаем выполнение метода
            System.out.println("Предупреждение: Epic с id " + subTask.epicId + " не существует. Подзадача не добавлена.");
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

    public void outputTask() {
        if (!taskHashMap.values().isEmpty()){
            System.out.println("Обычные задачи:");
            for (Task tasks : taskHashMap.values()){
                System.out.println(tasks.id +  ") " + tasks.title + "   " + tasks.status);
            }
        }
        else {
            System.out.println("Обычных задач пока нет");
        }
        if (!epicHashMap.values().isEmpty() && !subTaskHashMap.isEmpty()) {
            System.out.println("Подзадачи: ");
            for (Epic epics : epicHashMap.values()){
                System.out.println(epics.id +  ") "  + epics.title + "   " + epics.status);
                for (int subTask : epics.subTaskId){
                    System.out.println("   " + subTask + ") " + subTaskHashMap.get(subTask).title
                            + "   " + subTaskHashMap.get(subTask).status);
                }
            }
        }
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
        System.out.println("Все задачи были удалены");
    }

    public void removeTaskById(int id) {
        if(taskHashMap.containsKey(id)) {
            taskHashMap.remove(id);
        }
        else if (subTaskHashMap.containsKey(id)) {
            SubTask subTask = subTaskHashMap.get(id);
            Epic epic = epicHashMap.get(subTask.epicId);

            if(epic != null) {
                epic.subTaskId.remove((Integer) id);
                update(epic);
            }
            subTaskHashMap.remove(id);
        }
        else if (epicHashMap.containsKey(id)) {
            Epic epic = epicHashMap.get(id);
            for (Integer subTaskId : epic.subTaskId) {
                subTaskHashMap.remove(subTaskId);
            }
            epicHashMap.remove(id);

        }
        else {
            System.out.println("Такого id не сущетсвует");
        }
    }

    public void getTaskById(int id) {
        String findTask = null;
        if(taskHashMap.containsKey(id)){
            Task task = taskHashMap.get(id);
            findTask =  task.toString();
        }
        else if (subTaskHashMap.containsKey(id)){
            SubTask subTask = subTaskHashMap.get(id);
            findTask = subTask.toString();
        }
        else if (epicHashMap.containsKey(id)){
            Epic epic = epicHashMap.get(id);
            findTask = epic.toString();

        }
        else {
            findTask = "Такого id не сущетсвует";
        }
        System.out.println(findTask);
    }
}
