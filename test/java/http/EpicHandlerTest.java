package http;

import com.google.gson.Gson;
import controlles.Managers;
import controlles.TaskManager;
import model.Epic;
import model.SubTask;
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

public class EpicHandlerTest {
    private String START_URL = "http://localhost:8080/epics";
    private TaskManager taskManager;
    private HttpTaskServer taskServer;
    private HttpClient client;
    private Gson gson;

    private Epic epic1;
    private SubTask subTask1;
    private SubTask subTask2;
    private Epic epic2;
    private SubTask subTask3;

    public void initialization() {
        taskManager = Managers.getDefault();
        epic1 = new Epic("title1", "discription1", null, null);
        taskManager.add(epic1);
        subTask1 = new SubTask("title3", "discription3", epic1.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 3, 13, 15));
        subTask2 = new SubTask("title4", "discription4", epic1.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 6, 15, 15));
        taskManager.add(subTask1);
        taskManager.add(subTask2);

        epic2 = new Epic("title2", "discription2", null, null);
        taskManager.add(epic2);
        subTask3 = new SubTask("title5", "discription5", epic2.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 2, 17, 15));
        taskManager.add(subTask3);
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
        assertEquals(gson.toJson(taskManager.getEpics()), response.body(), "Не совпадает ожидаемый ответ");
    }

    @Test
    void getEpicByIdTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL + "/" + epic1.getId()))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200, "Не совпадает код статуса");
        assertEquals(gson.toJson(taskManager.getEpicsById(1)), response.body(), "Не совпадает ожидаемый ответ");
    }

    @Test
    void getEpicByNonExsistentId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL + "/10"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 404, "Не корректный результат при попытке получить " +
                "несуществующую задачу");
        assertEquals("Not Found", response.body(), "Не совпадает ожидаемый ответ");
    }

    @Test
    void getSubTaskByEpic() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL + "/" + epic1.getId() + "/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200, "Не корректный результат при попытке получить " +
                "список подзадач из эпиков");
        assertEquals(response.body(), gson.toJson(taskManager.getEpicsSupTask(epic1)), "Не совпадает ожидаемый ответ");
    }

    @Test
    void deleteEpicByNonExsistentId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL + "/8"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 404, "Не корректный результат при попытке удалить" +
                " несуществующую задачу");
        assertEquals("Not Found", response.body(), "Не совпадает ожидаемый ответ");
    }

    @Test
    void deleteEpicById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL + "/" + epic2.getId()))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200, "Не корректный результат при попытке удалить задачу");
        assertEquals(gson.toJson("Successfully"), response.body(), "Не совпадает ожидаемый ответ");
        // TODO - сделать проверку на корректное удаление подзадач, которые находились внутриудаляемого эпика
    }

    @Test
    void newEpic() throws IOException, InterruptedException {
        int sizeTasksBeforePost = taskManager.getEpics().size();
        System.out.println(sizeTasksBeforePost);
        Epic epic3 = new Epic("title3", "discription3",null, null);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(gson.toJson(epic3));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL))
                .POST(body1)
                .build();
        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(taskManager.getEpics());

        assertEquals(response1.statusCode(), 201, "Не корректный результат при попытке добавить эпик");
        assertEquals(sizeTasksBeforePost + 1, taskManager.getEpics().size(), "Не корректный " +
                "результат при попытке добавить эпик");
    }
}
