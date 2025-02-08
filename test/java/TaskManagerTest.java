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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

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
        SubTask subTask = new SubTask("title", "description", epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.now());
        manager.add(epic);
        manager.add(subTask);
        List<Task> subTasks = manager.getSubTasks();

        assertEquals(subTask.getStatus(), TaskStatus.NEW, "Не корректный статус при создании нового subTask");
        assertEquals(subTasks.size(), 2, "задача не записалась в менеджер");
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

        assertEquals(tasks.size(), 3, "задача не записалась в менеджер");
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

        manager.add(new SubTask("title3", "discription3",TaskStatus.IN_PROGRESS, subTask1.getId(),
                epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 3, 13, 15)));
        manager.add(subTask2);
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS, "Не правильный статус при изменении одной " +
                "задачи на IN_PROGRESS");

        SubTask newSubTask2 = new SubTask("title4", "discription4",TaskStatus.DONE, subTask2.getId(),
                epic.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 6, 14, 15));
        manager.update(newSubTask2);
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS, "Не правильный статус при изменении одной " +
                "задачи на IN_PROGRESS, а другой на Done");

        SubTask newSubTask1 = new SubTask("title3", "discription3",TaskStatus.DONE, subTask1.getId(),
                epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 3, 13, 15));
        manager.update(newSubTask1);
        assertEquals(epic.getStatus(), TaskStatus.DONE, "Не правильный статус при изменении одной " +
                "всех задач на Done");
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
    void getAllTasks() {

    }

    @Test
    void getTaskById() {

    }

    @Test
    void getAllEpics() {

    }

    @Test
    void getEpicById() {

    }

    @Test
    void getAllSubtask() {

    }

    @Test
    void getAllSubtaskById() {

    }
}
