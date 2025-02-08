import controlles.InMemoryTaskManager;
import controlles.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {
    static InMemoryTaskManager taskManager;
    static Task task;
    static Epic epic;
    static SubTask subTask;
    static SubTask subTask2;

    @BeforeEach
    void newTaskManager() {
        taskManager = new InMemoryTaskManager();
        task = new Task("Test addNewTask", "Test addNewTask description", Duration.ofMinutes(30), LocalDateTime.of(2025, 3, 5, 15, 15));
        epic = new Epic("Test addNewSubStac epic", "Test addNewSubTask epic description", 2, null, null);
        subTask = new SubTask("Test addNewSubTask", "Test add NewSubtask descriprion", 2, Duration.ofMinutes(12), LocalDateTime.of(2025, 3, 5, 14, 15));
        subTask2 = new SubTask("Test addNewSubTask", "Test add NewSubtask descriprion", TaskStatus.IN_PROGRESS, 2, 2, Duration.ofMinutes(12), LocalDateTime.of(2025, 3, 5, 14, 10));

        taskManager.add(task);
        taskManager.add(epic);
        taskManager.add(subTask);
        taskManager.add(subTask2);
    }

    @Test
    void addNewTask() {
        final Task savedTask = taskManager.getTaskByid(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final ArrayList<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");

        Task taskTwin = new Task("Task", "startTime equals task", Duration.ofMinutes(50),
                LocalDateTime.of(2025, 3, 5, 15, 12));
        taskManager.add(taskTwin);

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertFalse(taskManager.getTasks().contains(taskTwin), "Не корректная работа при пересечнии");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewSubTask() {
        final  SubTask savedSubTask = taskManager.getSubTaskById(subTask.getId());

        assertNotNull(savedSubTask, "Задача не найдена");
        assertEquals(savedSubTask, subTask, "Задачи не совпадают");
        assertEquals(savedSubTask.getStatus(), TaskStatus.NEW, "Статус задачи не совпадает");

        final ArrayList<Task> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Подзадачи не возвращаются");
        assertEquals(1, subTasks.size(), "Неверное количество задач, возможно неправильное добавление при пересечении");
        assertEquals(subTask, subTasks.get(0), "Задачи не совпадают");
    }

    @Test
    void addNewEpic() {
        //добавлены задачи с пересечением subTask и subTask2
        final Epic savedEpic = taskManager.getEpicsById(epic.getId());
        final ArrayList<Task> epics = taskManager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач");
        assertEquals(epic, epics.get(0), "Эпики не совпадают");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статусы эпиков не совпадают");

        //подзадача без пересечения
        subTask2 = new SubTask("Test addNewSubTask", "Test add NewSubtask descriprion", TaskStatus.DONE,
                2, epic.getId(), Duration.ofMinutes(5),
                LocalDateTime.of(2025, 3, 21, 13, 10));
        taskManager.add(subTask2);
        taskManager.update(epic);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статусы эпиков не совпадают");
        final ArrayList<SubTask> subTasks = taskManager.getEpicsSupTask(epic);
        assertNotNull(subTasks, "Подзадачи не возвращаются");
        assertEquals(2, subTasks.size(), "Выводится неверное количество подзадач");
        assertEquals(subTask2, subTasks.get(0), "Задачи в эпике не совпадают");

        SubTask subTaskUpdate = new SubTask("Test addNewSubTask", "Test add NewSubtask descriprion", TaskStatus.DONE,
                subTask.getId(), epic.getId(), Duration.ofMinutes(5),
                LocalDateTime.of(2025, 3, 21, 13, 10));
        taskManager.update(subTaskUpdate);
        taskManager.update(epic);

        assertEquals(TaskStatus.DONE, epic.getStatus(), "Статусы эпиков не совпадают");
        final ArrayList<SubTask> subTasks2 = taskManager.getEpicsSupTask(epic);
        assertNotNull(subTasks, "Подзадачи не возвращаются");
        assertEquals(2, subTasks.size(), "Выводится неверное количество подзадач");
        assertEquals(subTask2, subTasks.get(0), "Задачи в эпике не совпадают");
    }
}
