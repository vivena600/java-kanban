import controlles.FileBackedTaskManager;
import controlles.InMemoryTaskManager;
import controlles.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    @BeforeEach
    void input() {

    }

    @Test
    void addNewTask() {
        Task task = new Task("title", "description", Duration.ofMinutes(3), LocalDateTime.now());
        manager.add(task);
                List<Task> tasks = manager.getTasks();

        assertEquals(task.getStatus(), TaskStatus.NEW, "Не корректный статус при создании нового Task");
        assertEquals(tasks.size(), 1, "задача не записалась в менеджер");
    }

    @Test
    void addNewSubTask() {
        Epic epic = new Epic("title", "description", null, null);
        manager.add(epic);
        SubTask subTask = new SubTask("title", "description", epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.now());
        manager.add(subTask);
        List<Task> subTasks = manager.getSubTasks();

        assertEquals(subTask.getStatus(), TaskStatus.NEW, "Не корректный статус при создании нового subTask");
        assertEquals(subTasks.size(), 1, "задача не записалась в менеджер");
        assertEquals(subTasks.size(), 1, "задача не записалась в менеджер");
        System.out.println(subTasks.toString());
        assertEquals(epic.getSubTaskId().get(0), subTask.getId(), "У эпика не корректный вариант подзадачи");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("title", "description", null, null);
        manager.add(epic);
        List<Task> tasks = manager.getEpics();

        assertEquals(epic.getStatus(), TaskStatus.NEW, "Не корректный статус при создании нового Epic");
        assertEquals(tasks.size(), 1, "задача не записалась в менеджер");
        assertEquals(epic.getSubTaskId().size(), 0, "В Эпике не корректное число подзадач");

        SubTask subTask1 = new SubTask("title3", "discription3", epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 3, 13, 15));
        SubTask subTask2 = new SubTask("title4", "discription4", epic.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 6, 14, 15));
        manager.add(subTask1);
        manager.add(subTask2);

        assertEquals(tasks.size(), 1, "задача не записалась в менеджер");
        System.out.println(tasks.toString());
        assertEquals(epic.getSubTaskId().size(), 2, "В Эпике не корректное число подзадач");
    }

    @Test
    void statusEpic() {
        Epic epic = new Epic("title", "description", null, null);
        manager.add(epic);

        assertEquals(epic.getStatus(), TaskStatus.NEW, "Не корректный статус при создании нового Epic");

        SubTask subTask1 = new SubTask("title3", "discription3", epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 3, 13, 15));
        SubTask subTask2 = new SubTask("title4", "discription4", epic.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 6, 14, 15));

        manager.add(subTask1);
        SubTask newSubTask1 = new SubTask("title3", "discription3", TaskStatus.IN_PROGRESS,
                subTask1.getId(),  epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 3, 13, 15));
        manager.update(newSubTask1);
        manager.add(subTask2);
        manager.update(epic);
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS, "Не правильный статус при изменении одной " +
                "задачи на IN_PROGRESS");

        SubTask newSubTask2 = new SubTask("title4", "discription4",TaskStatus.DONE, subTask2.getId(),
                epic.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 6, 14, 15));
        manager.update(newSubTask2);
        manager.update(epic);
        System.out.println(manager.getSubTasks().toString());
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS, "Не правильный статус при изменении одной " +
                "задачи DONE");

        SubTask newSubTask = new SubTask("title3", "discription3", TaskStatus.DONE,
                newSubTask1.getId(),  epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 3, 13, 15));
        manager.update(newSubTask);
        manager.update(epic);
        System.out.println(manager.getSubTasks().toString());
        assertEquals(epic.getStatus(), TaskStatus.DONE, "Не правильный статус при изменении всех " +
                "задач DONE");
    }

    @Test
    void intersectionTask() {
        Epic epic = new Epic("title", "description", null, null);
        manager.add(epic);

        SubTask subTask1 = new SubTask("title3", "discription3", epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 6, 13, 15));
        SubTask subTask2 = new SubTask("title4", "discription4", epic.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 6, 13, 15));
        manager.add(subTask1);
        manager.add(subTask2);

        assertEquals(manager.getSubTasks().size(), 1, "записалол значение с совпадающим startTime для подзадач");
        assertEquals(epic.getSubTaskId().size(), 1, "В Эпике не корректное число подзадач");

        Task task1 = new Task("title5", "discription5", Duration.ofMinutes(12),
                LocalDateTime.of(2025, 5, 5, 5, 5));
        Task task2 = new Task("title6", "discription6", Duration.ofMinutes(12),
                LocalDateTime.of(2025, 5, 5, 5, 15));
        manager.add(task1);
        manager.add(task2);
        System.out.println(manager.getTasks().toString());
        assertEquals(manager.getTasks().size(), 1, "не коррректная запись при пересекающихся значений");
    }

    @Test
    void updateEndTime() {

    }

    @Test
    void deleteAllTask() {

    }

    @Test
    void deleteAllEpics() {

    }

    @Test
    void deleteAllSubtask() {

    }

    @Test
    void saveFile() throws IOException {

    }

    @Test
    void loadEmptyFile() throws IOException {

    }
}
