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

public class SubTaskHandlerTest {
    private String START_URL = "http://localhost:8080/subTasks";
    private TaskManager taskManager;
    private HttpTaskServer taskServer;
    private HttpClient client;
    private Gson gson;
    private SubTask subTask1;
    private SubTask subTask2;
    private Epic epic;

    public void initialization() {
        taskManager = Managers.getDefault();
        epic = new Epic("title3", "discription3", null, null);
        taskManager.add(epic);
        subTask1 = new SubTask("title3", "discription3", epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 3, 13, 15));
        subTask2 = new SubTask("title4", "discription4", epic.getId(), Duration.ofMinutes(12),
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
    void getSubTasksTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200, "Не совпадает код статуса");
        assertEquals(gson.toJson(taskManager.getSubTasks()), response.body(), "Не совпадает ожидаемый ответ");
    }

    @Test
    void getSubTaskByIdTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL + "/" + subTask1.getId()))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200, "Не совпадает код статуса");
        assertEquals(gson.toJson(subTask1), response.body(), "Не совпадает ожидаемый ответ");
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

    /*
    @Test
    void deleteTaskByNonExsistentId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL + "/5"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 404, "Не корректный результат при попытке удалить" +
                " несуществующую задачу");
        assertEquals("Not Found", response.body(), "Не совпадает ожидаемый ответ");
    }

    @Test
    void deleteTaskById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL + "/1"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200, "Не корректный результат при попытке удалить задачу");
        assertEquals(gson.toJson(taskManager.getTasks()), response.body(), "Не совпадает ожидаемый ответ");
    }

     */
}
