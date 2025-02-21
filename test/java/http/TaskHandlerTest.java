package http;

import com.google.gson.Gson;
import controlles.InMemoryTaskManager;
import controlles.Managers;
import controlles.TaskManager;
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

public class TaskHandlerTest {
    private String START_URL = "http://localhost:8080/tasks";
    private TaskManager taskManager;
    private HttpTaskServer taskServer;
    private HttpClient client;
    private Gson gson;

    public void initialization() {
        taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача 1", "Описание 1",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 01, 03, 00, 00));
        taskManager.add(task1);
        Task task2 = new Task("Задача 2", "Описание 2",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 01, 04, 00, 00));
        taskManager.add(task2);
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
    void getTasksTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200, "Не совпадает код статуса");
        assertEquals(gson.toJson(taskManager.getTasks()), response.body(), "Не совпадает ожидаемый ответ");
    }

    @Test
    void getTasByIdTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL + "/1"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200, "Не совпадает код статуса");
        assertEquals(gson.toJson(taskManager.getTaskByid(1)), response.body(), "Не совпадает ожидаемый ответ");
    }

    @Test
    void getTasByNonExsistentId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL + "/5"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 404, "Не корректный результат при попытке получить " +
                "несуществующую задачу");
        assertEquals("Not Found", response.body(), "Не совпадает ожидаемый ответ");
    }
}
