package test;

import controlles.InMemoryHistoryManager;
import controlles.InMemoryTaskManager;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    static InMemoryTaskManager taskManager;
    static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    Task task1 = new Task("title1", "discription1");
    Epic epic1 = new Epic("title2", "discription2");
    SubTask subTask1 = new SubTask("title3", "discription3", 5);
    SubTask subTask2 = new SubTask("title4", "discription4", 5);
    Epic epic2 = new Epic("title5", "discription5");
    SubTask subTask4 = new SubTask("title6", "discription6", 8);
    SubTask subTask5 = new SubTask("title7", "discription7", 8);
    SubTask subTask6 = new SubTask("title8", "discription8", 8);
    Task task2 = new Task("title9", "discription9");
    Task task3 = new Task("title10", "discription10");
    Task task4 = new Task("title11", "discription11");

    @BeforeEach
    void TaskManager(){
        taskManager = new InMemoryTaskManager();

        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(task3);
        taskManager.add(task4);
        taskManager.add(epic1);
        taskManager.add(subTask1);
        taskManager.add(subTask2);
        taskManager.add(epic2);
        taskManager.add(subTask4);
        taskManager.add(subTask5);
        taskManager.add(subTask6);
    }

    @Test
    void add(){
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
        assertEquals(11, taskManager.getHistory().size(), "Не корректный размер истории");
        assertEquals(task1, taskManager.getHistory().get(0), "Не корректно записываются элементы в начало " +
                "списка");
        assertEquals(subTask6, taskManager.getHistory().get(10), "Не корректно записываются элементы в конец " +
                "списка");
    }
    @Test
    void getHistoryWithSameTasks(){
        taskManager.getTaskByid(1);
        taskManager.getTaskByid(1);
        taskManager.getTaskByid(1);
        taskManager.getEpicsById(8);
        assertNotNull(taskManager.getHistory(), "Не получилось получить историю просмотров");
        assertEquals(2, taskManager.getHistory().size(), "Не корректный размер истории");
        assertEquals(task1, taskManager.getHistory().get(0), "Не корректно записываются элементы в начало " +
                "списка");
        assertEquals(epic2, taskManager.getHistory().get(1), "Не корректно записываются элементы в конец " +
                "списка");
    }

    @Test
    void getHistoryBeforeDeleteTask(){
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
        assertEquals(10, taskManager.getHistory().size(), "Не корректный размер истории");
        assertEquals(task2, taskManager.getHistory().get(0), "Не корректно записываются элементы в начало " +
                "списка");
        assertEquals(subTask6, taskManager.getHistory().get(9), "Не корректно записываются элементы в конец " +
                "списка");
        assertFalse(taskManager.getHistory().contains(task1), "Не корректно удалилась задача из истории");
    }

    @Test
    void getHistoryBeforeDeleteEpic(){
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
        assertEquals(7, taskManager.getHistory().size(), "Не корректный размер истории");
        assertEquals(task1, taskManager.getHistory().get(0), "Не корректно записываются элементы в начало " +
                "списка");
        assertEquals(subTask2, taskManager.getHistory().get(6), "Не корректно записываются элементы в конец " +
                "списка");
        assertFalse(taskManager.getHistory().contains(subTask4), "Не корректно удаляются подзадачи в эпиках");
        assertFalse(taskManager.getHistory().contains(epic2), "Не корректно удаляются эпики из истории");
    }
}
