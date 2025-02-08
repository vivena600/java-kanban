import controlles.InMemoryHistoryManager;
import controlles.InMemoryTaskManager;
import controlles.Managers;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private InMemoryTaskManager taskManager;
    private InMemoryHistoryManager historyManager;

    Task task1 = new Task("title1", "discription1", Duration.ofMinutes(12), LocalDateTime.of(2025, 3, 5, 14, 15));
    Epic epic1 = new Epic("title2", "discription2", null, null);
    SubTask subTask1 = new SubTask("title3", "discription3", 5, Duration.ofMinutes(3), LocalDateTime.of(2025, 3, 3, 13, 15));
    SubTask subTask2 = new SubTask("title4", "discription4", 5, Duration.ofMinutes(12), LocalDateTime.of(2025, 3, 6, 14, 15));
    Epic epic2 = new Epic("title5", "discription5", null, null);
    SubTask subTask4 = new SubTask("title6", "discription6", 8, Duration.ofMinutes(12), LocalDateTime.of(2025, 3, 3, 13, 15)); //cовпадает с subTask1
    SubTask subTask5 = new SubTask("title7", "discription7", 8, Duration.ofMinutes(12), LocalDateTime.of(2024, 3, 5, 14, 15));
    SubTask subTask6 = new SubTask("title8", "discription8", 8, Duration.ofMinutes(12), LocalDateTime.of(2026, 3, 5, 14, 15));
    Task task2 = new Task("title9", "discription9", Duration.ofMinutes(12),
            LocalDateTime.of(2025, 3, 5, 14, 15)); //совпадает с task1
    Task task3 = new Task("title10", "discription10", Duration.ofMinutes(3), LocalDateTime.of(2023, 3, 3, 3, 33));
    Task task4 = new Task("title11", "discription11", Duration.ofMinutes(4), LocalDateTime.of(2025, 4, 4, 4, 4));

    @BeforeEach
    void taskManager() {
        taskManager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
        taskManager.add(task1);
        taskManager.add(task2); //не должен добавиться из-за ограничений
        taskManager.add(task3);
        taskManager.add(task4);
        taskManager.add(epic1);
        taskManager.add(subTask1);
        taskManager.add(subTask2);
        taskManager.add(epic2);
        taskManager.add(subTask4); //не добавится
        taskManager.add(subTask5);
        taskManager.add(subTask6);
    }

    @Test
    void empityHistoryTest() {
        final ArrayList<Task> historyTask = historyManager.getHistoryTask();
        assertEquals(0, historyTask.size(), "Исория должна быть пустой");
    }

    @Test
    void add() {
        historyManager.add(task2);
        final ArrayList<Task> historyTask = historyManager.getHistoryTask();
        assertNotNull(historyTask, "Итория не пустая");
        assertEquals(1, historyTask.size(), "История не пустая.");
    }

    @Test
    void getHistory() {
        taskManager.getTaskByid(1);
        taskManager.getTaskByid(2);
        taskManager.getTaskByid(3);
        taskManager.getTaskByid(4);
        taskManager.getEpicsById(5);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(7);
        taskManager.getEpicsById(8);
        taskManager.getSubTaskById(9);
        taskManager.getSubTaskById(10);
        taskManager.getSubTaskById(11);

        assertNotNull(taskManager.getHistory(), "Не получилось получить историю просмотров");
        assertEquals(9, taskManager.getHistory().size(), "Не корректный размер истории");
        assertEquals(task1, taskManager.getHistory().get(0), "Не корректно записываются элементы в начало " +
                "списка");
        assertEquals(subTask6, taskManager.getHistory().get(8), "Не корректно записываются элементы в конец " +
                "списка");
    }

    @Test
    void getHistoryWithSameTasks() {
        taskManager.getTaskByid(task1.getId());
        taskManager.getTaskByid(task1.getId());
        taskManager.getTaskByid(task1.getId());
        taskManager.getEpicsById(8);

        System.out.println(taskManager.getHistory().toString());
        System.out.println(taskManager.getHistory().size());
        assertNotNull(taskManager.getHistory(), "Не получилось получить историю просмотров");
        assertEquals(2, taskManager.getHistory().size(), "Не корректный размер истории");
        assertEquals(task1, taskManager.getHistory().get(0), "Не корректно записываются элементы в начало " +
                "списка");
        assertEquals(epic2, taskManager.getHistory().get(1), "Не корректно записываются элементы в конец " +
                "списка");
    }

    @Test
    void getHistoryBeforeDeleteTask() {
        taskManager.getTaskByid(1);
        taskManager.getTaskByid(2);
        taskManager.getTaskByid(3);
        taskManager.getTaskByid(4);
        taskManager.getEpicsById(5);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(7);
        taskManager.getEpicsById(8);
        taskManager.getSubTaskById(9);
        taskManager.getSubTaskById(10);
        taskManager.getSubTaskById(11);

        taskManager.deleteTasks(1);
        assertNotNull(taskManager.getHistory(), "Не получилось получить историю просмотров");
        assertEquals(8, taskManager.getHistory().size(), "Не корректный размер истории");
        assertEquals(task3, taskManager.getHistory().get(0), "Не корректно записываются элементы в начало " +
                "списка");
        assertEquals(subTask6, taskManager.getHistory().get(7), "Не корректно записываются элементы в конец " +
                "списка");
        assertFalse(taskManager.getHistory().contains(task1), "Не корректно удалилась задача из истории");
    }

    @Test
    void getHistoryBeforeDeleteEpic() {
        taskManager.getTaskByid(1);
        taskManager.getTaskByid(2);
        taskManager.getTaskByid(3);
        taskManager.getTaskByid(4);
        taskManager.getEpicsById(5);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(7);
        taskManager.getEpicsById(8);
        taskManager.getSubTaskById(9);
        taskManager.getSubTaskById(10);
        taskManager.getSubTaskById(11);

        taskManager.deleteEpics(8);
        assertNotNull(taskManager.getHistory(), "Не получилось получить историю просмотров");
        assertEquals(6, taskManager.getHistory().size(), "Не корректный размер истории");
        assertEquals(task1, taskManager.getHistory().get(0), "Не корректно записываются элементы в начало " +
                "списка");
        assertEquals(subTask2, taskManager.getHistory().get(5), "Не корректно записываются элементы в конец " +
                "списка");
        assertFalse(taskManager.getHistory().contains(subTask4), "Не корректно удаляются подзадачи в эпиках");
        assertFalse(taskManager.getHistory().contains(epic2), "Не корректно удаляются эпики из истории");
    }

    @Test
    void getHistoryTaskBeforeDeleteSimpleTasks() {
        System.out.println(taskManager.getHistory().size());
        taskManager.getTaskByid(1);
        taskManager.getTaskByid(2);
        taskManager.getTaskByid(3);
        taskManager.getTaskByid(4);
        taskManager.getEpicsById(5);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(7);
        taskManager.getEpicsById(8);
        taskManager.getSubTaskById(9);
        taskManager.getSubTaskById(10);
        taskManager.getSubTaskById(11);
        System.out.println(taskManager.getHistory().size());

        taskManager.deleteTask();
        System.out.println(taskManager.getHistory().size());
        System.out.println(taskManager.getHistory().toString());
        assertNotNull(taskManager.getHistory(), "Не получилось получить историю просмотров");
        assertEquals(6, taskManager.getHistory().size(), "Не корректный размер истории после " +
                "удаления всех задач");
        assertFalse(taskManager.getHistory().contains(task1), "Не удалились одна из задач");
        assertEquals(epic1, taskManager.getHistory().get(0), "Не корректная запись первого элемента");
    }

    @Test
    void getHistoryTasksBeforeDeleteEpics() {
        taskManager.getTaskByid(1);
        taskManager.getTaskByid(2);
        taskManager.getTaskByid(3);
        taskManager.getTaskByid(4);
        taskManager.getEpicsById(5);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(7);
        taskManager.getEpicsById(8);
        taskManager.getSubTaskById(9);
        taskManager.getSubTaskById(10);
        taskManager.getSubTaskById(11);

        taskManager.deleteEpic();
        assertNotNull(taskManager.getHistory(), "Не получилось получить историю просмотров");
        assertEquals(3, taskManager.getHistory().size(), "Не корректный размер истории после " +
                "удаления всех задач");
        assertFalse(taskManager.getHistory().contains(subTask4), "Не удалились подзадачи в эпиках");
        assertEquals(task4, taskManager.getHistory().get(2), "Не корректная запись последнего элемента");
    }
}
