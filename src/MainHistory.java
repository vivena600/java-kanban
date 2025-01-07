import controlles.*;
import model.*;

import java.util.ArrayList;

public class MainHistory {
    static TaskManager taskManager;
    public static void main(String[] args) {
        taskManager = Managers.getDefault();
        Task task1 = new Task("Задача 1", "Описание 1"); //id = 1
        Task task2 = new Task("Задача 2", "Описание 2"); //id = 2
        taskManager.add(task1);
        taskManager.add(task2);
        Epic epic1 = new Epic("Эпик 1", "эпик с 3 подзадачами"); //id = 3
        Epic epic2 = new Epic("Эпик 2", "эпик без задач"); //id = 4
        taskManager.add(epic1);
        taskManager.add(epic2);
        SubTask subTask1 = new SubTask("подзадача 1", "описание 1", epic1.getId()); //id = 5
        SubTask subTask2 = new SubTask("подзадача 2", "описание 2", epic1.getId()); //id = 6
        SubTask subTask3 = new SubTask("подзадача 3", "описание 3", epic1.getId()); //id = 7
        taskManager.add(subTask1);
        taskManager.add(subTask2);
        taskManager.add(subTask3);

        printAllTasks(taskManager);
        System.out.println("-".repeat(120));
        System.out.println("Вывод истории с повторяющимися задачами с id 6, 1 и 2");
        taskManager.getTaskByid(1);
        taskManager.getTaskByid(2);
        taskManager.getEpicsById(3);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(7);
        taskManager.getEpicsById(4);
        //значения ниже повторяются
        taskManager.getSubTaskById(6);
        taskManager.getTaskByid(1);
        taskManager.getTaskByid(2);
        printHistory(taskManager);
        System.out.println("-".repeat(120));
        System.out.println("Вывод истории после удаления задачи с id = 1");
        taskManager.deleteTasks(1);
        printHistory(taskManager);
        System.out.println("-".repeat(120));
        System.out.println("Вывод истории после удаления эпика с тремя подзадачами (id 3)");
        taskManager.deleteEpics(3);
        printHistory(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);
            for (Task task : manager.getEpicsSupTask((Epic) epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTasks()) {
            System.out.println(subtask);
        }
    }

    public static void printHistory(TaskManager taskManager) {
        ArrayList<Task> history = taskManager.getHistory();
        int index = 1;
        for (Task task : history) {
            System.out.println(index + "  " + task);
            index++;
        }
    }
}