package http;

import com.google.gson.Gson;
import controlles.InMemoryTaskManager;
import controlles.Managers;
import controlles.TaskManager;
import model.Task;
import model.TaskStatus;
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

    private Task task1;
    private Task task2;

    public void initialization() {
        taskManager = new InMemoryTaskManager();
        task1 = new Task("Задача 1", "Описание 1",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 01, 03, 00, 00));
        taskManager.add(task1);
        task2 = new Task("Задача 2", "Описание 2",  Duration.ofMinutes(35),
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
    void getTaskByNonExsistentId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL + "/5"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 404, "Не корректный результат при попытке получить " +
                "несуществующую задачу");
        assertEquals("Not Found", response.body(), "Не совпадает ожидаемый ответ");
    }

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

    @Test
    void newTask() throws IOException, InterruptedException {
        int sizeTasksBeforePost = taskManager.getTasks().size();
        Task task3 = new Task("Задача 3", "Описание 3",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 01, 05, 06, 00));
        Task task4 = new Task("Задача 4", "Описание 4",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 01, 05, 06, 00));
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(gson.toJson(task3));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL))
                .POST(body1)
                .build();
        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response1.statusCode(), 201, "Не корректный результат при попытке удалить задачу");
        assertEquals(sizeTasksBeforePost + 1, taskManager.getTasks().size(), "Не корректный " +
                "результат при попытке добавить задачу");

        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(gson.toJson(task4));
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(START_URL))
                .POST(body2)
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(response2.statusCode(), 406, "Не корректный результат при попытке добавить " +
                "повторяющуюся задачу");
        assertEquals(sizeTasksBeforePost + 1, taskManager.getTasks().size(), "Не корректный " +
                "результат при попытке добавить задачу");
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        int sizeTasksBeforePost = taskManager.getTasks().size();
        Task newTask1 = new Task("Задача 1", "Описание 1", TaskStatus.DONE, task1.getId(), Duration.ofMinutes(35),
                LocalDateTime.of(2025, 12, 03, 00, 00));
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(gson.toJson(newTask1));
        URI url = URI.create(START_URL + "/" + task1.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body1)
                .build();
        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response1.statusCode(), 201, "Не корректный результат при попытке изменить задачу");
        assertEquals(sizeTasksBeforePost, taskManager.getTasks().size(), "Не корректный " +
                "результат при попытке изменить задачу");
        assertEquals(newTask1, taskManager.getTaskByid(task1.getId()), "Не корректный update для задачи");

        int sizeTasksBeforePost2 = taskManager.getTasks().size();
        Task newTask2 = new Task("Задача 2", "Описание 2", TaskStatus.DONE, task2.getId(),
                Duration.ofMinutes(35),
                LocalDateTime.of(2025, 12, 03, 00, 00));
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(gson.toJson(newTask1));
        URI url2 = URI.create(START_URL + "/" + task2.getId());
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(body2)
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(response2.statusCode(), 406, "Не корректный результат при попытке изменить " +
                "задачу, так как время пересекается с другими задачами");
    }
}
