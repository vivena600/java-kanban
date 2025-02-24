package http;

import com.google.gson.Gson;
import controlles.InMemoryTaskManager;
import controlles.Managers;
import controlles.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryHandlerTest {
    private final String START_URL = "http://localhost:8080/history";
    private TaskManager taskManager;
    private HttpTaskServer taskServer;
    private HttpClient client;
    private Gson gson;

    private Task task1;
    private Task task2;
    private Epic epic1;
    private SubTask subTask1;
    private SubTask subTask2;

    public void initialization() {
        taskManager = new InMemoryTaskManager();
        task1 = new Task("Задача 1", "Описание 1",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 01, 03, 00, 00));
        taskManager.add(task1);
        task2 = new Task("Задача 2", "Описание 2",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 01, 04, 00, 00));
        taskManager.add(task2);

        epic1 = new Epic("title1", "discription1", null, null);
        taskManager.add(epic1);
        subTask1 = new SubTask("title3", "discription3", epic1.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 3, 13, 15));
        subTask2 = new SubTask("title4", "discription4", epic1.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 6, 15, 15));
        taskManager.add(subTask1);
        taskManager.add(subTask2);
    }

    @BeforeEach
    void startedServer () throws IOException {
        initialization();
        gson = Managers.getJson();
        taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void stopServer() {
        taskServer.stop();
    }

    @Test
    void getHistoryIsEmptyTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200, "Не совпадает код статуса");
        assertEquals(gson.toJson(taskManager.getHistory()), response.body(), "Не совпадает вывод " +
                " пустой истории");
        assertEquals(taskManager.getHistory().size(), 0);
    }

    @Test
    void getHistoryIsNotEmptyTest() throws IOException, InterruptedException {
        taskManager.getTaskByid(task2.getId()); //3 задачи
        taskManager.getTaskByid(task1.getId());
        taskManager.getEpicsById(epic1.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200, "Не совпадает код статуса");
        assertEquals(gson.toJson(taskManager.getHistory()), response.body(), "Не совпадает вывод истории");
       assertEquals(taskManager.getHistory().size(), 3);

        taskManager.deleteTask(task2.getId());
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(START_URL))
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(response2.statusCode(), 200, "Не совпадает код статуса");
        assertEquals(gson.toJson(taskManager.getHistory()), response2.body(), "Не совпадает вывод истории");
        assertEquals(taskManager.getHistory().size(), 2);
    }
}
