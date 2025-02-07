import model.*;
import controlles.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    /*
    static InMemoryTaskManager taskManager;
    static Task task;
    static Epic epic;
    static SubTask subTask;
    static SubTask subTask2;

    @BeforeAll
    static void newTaskManager() {
        taskManager = new InMemoryTaskManager();
        task = new Task("Test addNewTask", "Test addNewTask description");
        epic = new Epic("Test addNewSubStac epic", "Test addNewSubTask epic description", 2);
        subTask = new SubTask("Test addNewSubTask", "Test add NewSubtask descriprion", 2);
        subTask2 = new SubTask("Test addNewSubTask", "Test add NewSubtask descriprion",
                TaskStatus.IN_PROGRESS, 2, 2);
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
    void addNewSubTask() {
        taskManager.add(subTask);
        final  SubTask savedSubTask = taskManager.getSubTaskById(subTask.getId());

        assertNotNull(savedSubTask, "Задача не найдена");
        assertEquals(savedSubTask, subTask, "Задачи не совпадают");
        assertEquals(savedSubTask.getStatus(), TaskStatus.NEW, "Статус задачи не совпадает");

        final ArrayList<Task> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Подзадачи не возвращаются");
        assertEquals(1, subTasks.size(), "Неверное количество задач");
        assertEquals(subTask, subTasks.get(0), "Задачи не совпадают");
    }

    @Test
    void addNewEpic() {
        taskManager.add(epic);
        taskManager.add(subTask);
        final Epic savedEpic = taskManager.getEpicsById(epic.getId());
        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(savedEpic, epic, "Задачи не совпадают");
        subTask2 = new SubTask("Test addNewSubTask", "Test add NewSubtask descriprion", TaskStatus.DONE,
                2, 1);
        taskManager.add(subTask2);
        taskManager.update(epic);
        assertEquals(TaskStatus.DONE, subTask2.getStatus(), "Статусы эпиков не совпадают");
        final ArrayList<Task> epics = taskManager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач");
        assertEquals(epic, epics.get(0), "Эпики не совпадают");
        final ArrayList<SubTask> subTasks = taskManager.getEpicsSupTask(epic);
        assertNotNull(subTasks, "Подзадачи не возвращаются");
        assertEquals(1, subTasks.size(), "Выводится неверное количество подзадач");
        assertEquals(subTask2, subTasks.get(0), "Задачи в эпике не совпадают");
    }

    @Test
    void deleteById() {
        taskManager.deleteTasks(task.getId());
        System.out.println(taskManager.getTasks());
        assertEquals(0, taskManager.getTasks().size(), "Задача не удалилась");
        taskManager.deleteSubtasks(subTask2.getId());
        taskManager.update(epic);
        assertEquals(1, taskManager.getSubTasks().size(), "Подзадача не удалилась"); //до этого в эпике
        //было две задачи SubTask и SubTask2
        Epic epic2 = new Epic("Test deleteById epic", "Test deleteById epic description");
        taskManager.add(epic2);
        SubTask subTask3 = new SubTask("Test deleteById", "Test add deleteById descriprion",
                epic2.getId());
        taskManager.add(subTask3);
        taskManager.deleteEpics(4);
        assertEquals(1, taskManager.getEpics().size(), "Не удалось удалить эпик");
    }

     */
}