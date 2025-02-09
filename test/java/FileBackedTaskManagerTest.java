import controlles.FileBackedTaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{
    File file;
    Task task1 = new Task("title1", "discription1", Duration.ofMinutes(1),
            LocalDateTime.of(2025, 3, 6, 13, 15));
    Epic epic1 = new Epic("title2", "discription2", null, null);
    SubTask subTask1 = new SubTask("title3", "discription3",epic1.getId(), Duration.ofMinutes(2),
            LocalDateTime.of(2025, 5, 5, 4, 4));
    /*
    Task task2 = new Task("title9", "discription9");
    Task task3 = new Task("title10", "discription10");
    Task task4 = new Task("title11", "discription11");

     */

    @BeforeEach
    void initialization() throws IOException {
        file = File.createTempFile("FileBackedTest", "csv");
        manager = new FileBackedTaskManager(file.getName());
    }

    @Override
    @Test
    void saveFile() throws IOException {
        FileBackedTaskManager fileBacked1 = new FileBackedTaskManager(file.getAbsolutePath());
        fileBacked1.add(task1);
        List<String> readFile = Files.readAllLines(file.toPath());
        assertNotNull(readFile, "файл пустой");
        assertTrue(file.exists(), "файл не был сохранен");
        assertEquals(readFile.size(), 2, "Не совпадает колчисетво строк");
    }

    @Override
    @Test
    void loadEmptyFile() throws IOException {
        List<String> readFile = Files.readAllLines(file.toPath());
        assertEquals(readFile.size(), 0, "Не правильная работа для пустого файла");
    }
}
