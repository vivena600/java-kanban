import model.*;
import controlles.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Дописать код", "для 4-го спринта");
        taskManager.add(task1);
        Epic epic1 = new Epic("Дописать курсовую", "Иначе у тебя незачёт) ");
        taskManager.add(epic1);
        SubTask subTask1 = new SubTask("Прочитать теорию", "выбрать метод хеширования", epic1.getId());
        taskManager.add(subTask1);
        SubTask subTask2 = new SubTask("Написать код", "", epic1.getId());
        taskManager.add(subTask2);
        Epic epic2 = new Epic("эпик2", "описание");
        taskManager.add(epic2);
        SubTask subTask3 = new SubTask("задача1", "описание", epic2.getId());
        taskManager.add(subTask3);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getEpicsSupTask(epic1));
        System.out.println(taskManager.getSubTasks());

        Task task1Update = new Task(task1.getTitle(), task1.getDescription(), TaskStatus.IN_PROGRESS, task1.getId());
        taskManager.update(task1Update);

        SubTask subTaskUpdate = new SubTask(subTask3.getTitle(), subTask3.getDescription(), TaskStatus.DONE, subTask3.getId(),
                subTask3.getEpicId());
        taskManager.update(subTaskUpdate);
        taskManager.update(epic2);
    }
}