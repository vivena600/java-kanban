import controlles.*;
import model.*;

import java.util.ArrayList;

public class Main {
    static TaskManager taskManager;
    public static void main(String[] args) {
        taskManager = Managers.getDefault();

        Task task1 = new Task("Дописать код", "для 5-го спринта");
        taskManager.add(task1);
        Epic epic1 = new Epic("Дописать курсовую", "Осталось только сдать ПЗ ");
        taskManager.add(epic1);
        SubTask subTask1 = new SubTask("Написать титульный лист", "по ГОСТу", epic1.getId());
        taskManager.add(subTask1);
        SubTask subTask2 = new SubTask("Написать теоретическую часть ", "", epic1.getId());
        taskManager.add(subTask2);
        Epic epic2 = new Epic("эпик2", "описание");
        taskManager.add(epic2);
        SubTask subTask3 = new SubTask("задача1", "описание", epic2.getId());
        taskManager.add(subTask3);

        Task task1Update = new Task(task1.getTitle(), task1.getDescription(), TaskStatus.IN_PROGRESS, task1.getId());
        taskManager.update(task1Update);

        SubTask subTaskUpdate = new SubTask(subTask3.getTitle(), subTask3.getDescription(), TaskStatus.DONE, subTask3.getId(),
                subTask3.getEpicId());
        taskManager.update(subTaskUpdate);
        taskManager.update(epic2);

        Epic epic3 = new Epic("эпик 3", "описание 3");
        taskManager.add(epic3);

        SubTask subTask4 = new SubTask("подзадача 1", "описание 1", epic3.getId());
        SubTask subTask5 = new SubTask("подзадача 2", "описание 2", epic3.getId());
        SubTask subTask6 = new SubTask("подзадача 3", "описание 3", epic3.getId());
        taskManager.add(subTask4);
        taskManager.add(subTask5);
        taskManager.add(subTask6);

        printAllTasks(taskManager);
        System.out.println("-".repeat(120));
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

    public static void printHistory(TaskManager taskManager){
        taskManager.getTaskByid(1);
        taskManager.getEpicsById(2);
        taskManager.getSubTaskById(3);
        taskManager.getSubTaskById(4);
        taskManager.getEpicsById(5);
        taskManager.getSubTaskById(6);
        taskManager.getEpicsById(7);
        taskManager.getSubTaskById(8);
        taskManager.getSubTaskById(9);
        taskManager.getSubTaskById(10);
        taskManager.getTaskByid(1);

        ArrayList<Task> history = taskManager.getHistory();
        int index = 1;
        for(Task task : history){
            System.out.println(index + "  " + task);
            index ++;
        }
    }
}