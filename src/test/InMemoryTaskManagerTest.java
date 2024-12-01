package model;

import controlles.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    static InMemoryTaskManager taskManager;
    static Task task;
    static Epic epic;
    static SubTask subTask;

    @BeforeAll
    static void newTaskManager() {
        taskManager = new InMemoryTaskManager();
        task = new Task("Test addNewTask", "Test addNewTask description");
        epic = new Epic("Test addNewSubStac epic", "Test addNewSubTask epic description", 1);
        subTask = new SubTask("Test addNewSubTask", "Test add NewSubtask descriprion", 1);
    }

    @Test
    void addNewTask() {
        taskManager.add(task);
        final Task savedTask = taskManager.getTaskByid(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final ArrayList<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewSubTask(){
        taskManager.add(subTask);
        final  SubTask savedSubTask = taskManager.getSubTaskById(subTask.getId());

        assertNotNull(savedSubTask, "Задача не найдена");
        assertEquals(savedSubTask, subTask, "Задачи не совпадают");
        assertEquals(savedSubTask.getStatus(), TaskStatus.NEW, "Статус задачи не совпадает");

        final ArrayList<Task> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Под задачи не возвращаются");
        assertEquals(1, subTasks.size(), "Неверное количество задач");
        assertEquals(subTask, subTasks.get(0), "Задачи не совпадают");
    }
}