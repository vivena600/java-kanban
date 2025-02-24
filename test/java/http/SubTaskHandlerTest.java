package http;

import com.google.gson.Gson;
import controlles.Managers;
import controlles.TaskManager;
import model.Epic;
import model.SubTask;
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

public class SubTaskHandlerTest {
    private static final String START_URL = "http://localhost:8080/subTasks";
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
                .uri(URI.create(START_URL + "/" + subTask1.getId()))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200, "Не корректный результат при попытке удалить задачу");
        assertEquals(gson.toJson(taskManager.getSubTasks()), response.body(), "Не совпадает ожидаемый ответ");
    }

    @Test
    void newSubTask() throws IOException, InterruptedException {
        int sizeTasksBeforePost = taskManager.getSubTasks().size();
        SubTask subTask3 = new SubTask("title4", "discription4", epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 12, 3, 13, 15));
        SubTask subTask4 = new SubTask("title5", "discription5", epic.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 6, 15, 15));
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(gson.toJson(subTask3));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(START_URL))
                .POST(body1)
                .build();
        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response1.statusCode(), 201, "Не корректный результат при попытке добавить" +
                " подзадачу");
        assertEquals(sizeTasksBeforePost + 1, taskManager.getSubTasks().size(), "Не корректный " +
                "результат при попытке добавить подзадачу");

        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(gson.toJson(subTask4));
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(START_URL))
                .POST(body2)
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(response2.statusCode(), 406, "Не корректный результат при попытке добавить " +
                "повторяющуюся задачу");
        assertEquals(epic.getSubTaskId().size(), 3, "Не корректное количество подзадач в эпике");
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        int sizeTasksBeforePost = taskManager.getSubTasks().size();
        SubTask newSubTask1 = new SubTask("title3", "discription3", TaskStatus.DONE, subTask1.getId(),
                epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 12, 3, 13, 15));
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(gson.toJson(newSubTask1));
        URI url = URI.create(START_URL + "/" + subTask1.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body1)
                .build();
        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response1.statusCode(), 201, "Не корректный результат при попытке изменить задачу");
        assertEquals(sizeTasksBeforePost, taskManager.getSubTasks().size(), "Не корректный " +
                "результат при попытке изменить подзадачу");
        assertEquals(newSubTask1, taskManager.getSubTaskById(subTask1.getId()), "Не корректный update" +
                " для подзадачи");

        SubTask newSubTask2 = new SubTask("title4", "discription4", epic.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 6, 15, 15));
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(gson.toJson(newSubTask2));
        URI url2 = URI.create(START_URL + "/" + subTask2.getId());
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .POST(body2)
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(response2.statusCode(), 406, "Не корректный результат при попытке изменить " +
                "подзадачу, так как время пересекается с другими задачами");
    }
}
