import controlles.Managers;
import controlles.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class NewMainHistory {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        //InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task2 = new Task("Задача 2", "Описание 2",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 01, 04, 00, 00));
        Task task1 = new Task("Задача 1", "Описание 1", Duration.ofSeconds(50), null);
        Task task3 = new Task("Задача 2", "Описание 2",  Duration.ofMinutes(35),
                LocalDateTime.of(2026, 02, 04, 00, 00)); //id = 2
        taskManager.add(task2);
        taskManager.add(task1);
        taskManager.add(task3);

        Epic epic1 = new Epic("Эпик 1", "эпик с 3 подзадачами", null, null); //id = 3
        Epic epic2 = new Epic("Эпик 2", "эпик без задач",  null, null); //id = 4
        taskManager.add(epic1);
        taskManager.add(epic2);
        SubTask subTask1 = new SubTask("подзадача 1", "описание 1", epic1.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 02, 03, 00, 00)); //id = 5
        SubTask subTask2 = new SubTask("подзадача 2", "описание 2", epic1.getId(), Duration.ofMinutes(4),
                LocalDateTime.of(2025, 02, 03, 00, 00)); //id = 6
        SubTask subTask3 = new SubTask("подзадача 3", "описание 3", epic1.getId(), Duration.ofMinutes(5),
                LocalDateTime.of(2025, 02, 03, 00, 00)); //id = 7
        taskManager.add(subTask1);
        taskManager.add(subTask2);
        taskManager.add(subTask3);

        taskManager.getTaskByid(task1.getId());
        taskManager.getTaskByid(task2.getId());
        taskManager.getTaskByid(task2.getId());
        taskManager.getTaskByid(task3.getId());

        taskManager.getSubTaskById(subTask1.getId());
        taskManager.getSubTaskById(subTask2.getId());
        taskManager.getSubTaskById(subTask3.getId());

        taskManager.getEpicsById(epic1.getId());
        taskManager.getEpicsById(epic2.getId());

        taskManager.deleteTasks();
        System.out.println(taskManager.getHistory().toString());
        System.out.println(taskManager.getHistory().size());
        System.out.println(taskManager.getTasks().toString());
    }
}
