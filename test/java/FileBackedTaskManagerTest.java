import controlles.FileBackedTaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    /*
    File file;
    Task task1 = new Task("title1", "discription1");
    Epic epic1 = new Epic("title2", "discription2");
    SubTask subTask1 = new SubTask("title3", "discription3", epic1.getId());
    SubTask subTask2 = new SubTask("title4", "discription4", epic1.getId());
    Epic epic2 = new Epic("title5", "discription5");
    SubTask subTask3 = new SubTask("title6", "discription6", 8);
    SubTask subTask4 = new SubTask("title7", "discription7", 8);
    SubTask subTask5 = new SubTask("title8", "discription8", 8);
    Task task2 = new Task("title9", "discription9");
    Task task3 = new Task("title10", "discription10");
    Task task4 = new Task("title11", "discription11");

    @BeforeEach
    void initialization() throws IOException {
        file = File.createTempFile("FileBackedTest", "csv");
    }

    @Test
    void saveFile() throws IOException {
        FileBackedTaskManager fileBacked1 = new FileBackedTaskManager(file.getAbsolutePath());
        fileBacked1.add(task1);
        List<String> readFile = Files.readAllLines(file.toPath());
        assertNotNull(readFile, "файл пустой");
        assertTrue(file.exists(), "файл не был сохранен");
        assertEquals(readFile.size(), 2, "Не совпадает колчисетво строк");
    }

    @Test
    void addAndUpdateTask() throws IOException {
        FileBackedTaskManager fileBacked2 = new FileBackedTaskManager(file.getAbsolutePath());
        fileBacked2.add(task1);
        fileBacked2.add(task2);
        fileBacked2.add(task3);
        fileBacked2.add(task4);
        Task task2Update = new Task("Задача 2", "Описание 2", TaskStatus.DONE, task2.getId());
        fileBacked2.update(task2Update);
        List<String> readFile = Files.readAllLines(file.toPath());
        assertNotNull(readFile, "файл пустой");
        assertTrue(file.exists(), "файл не был сохранен");
        assertEquals(readFile.size(), 5, "Не совпадает колчисетво строк");
        assertEquals(readFile.get(1), "1,TASK,title1,NEW,discription1",
                "Сохранется неправильное значение строк");
        assertEquals(readFile.get(2), "2,TASK,Задача 2,DONE,Описание 2",
                "Сохранется неправильное значение строк после изменения задачи");
    }

    @Test
    void deleteTaskById() throws IOException {
        FileBackedTaskManager fileBacked3 = new FileBackedTaskManager(file.getAbsolutePath());
        fileBacked3.add(task1);
        fileBacked3.add(task2);
        fileBacked3.add(task3);
        fileBacked3.deleteTasks(1);
        List<String> readFile = Files.readAllLines(file.toPath());
        assertTrue(file.exists(), "файл не был сохранен");
        assertEquals(readFile.size(), 3, "Не совпадает количество записей в файле после удаления" +
                " задачи по id");
        fileBacked3.deleteTask();
        readFile = Files.readAllLines(file.toPath());
        assertEquals(readFile.size(), 1, "Не корректно удаляются всеx задач");
    }

    @Test
    void addSubtaskAndEpic() throws IOException {
        FileBackedTaskManager fileBacked4 = new FileBackedTaskManager(file.getAbsolutePath());
        fileBacked4.add(epic1);
        fileBacked4.add(epic2);
        fileBacked4.add(subTask1);
        fileBacked4.add(subTask2);
        fileBacked4.add(subTask3);
        fileBacked4.add(subTask4);
        fileBacked4.add(subTask5);
        List<String> readFile = Files.readAllLines(file.toPath());
        assertTrue(file.exists(), "файл не был сохранен");
        assertEquals(readFile.size(), 8, "Не корректно добавляются эпики и подзадачи в файл");
    }

     */
}
