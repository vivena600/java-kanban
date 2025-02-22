package server.checkUpHandler;

import com.google.gson.Gson;
import controlles.Managers;
import controlles.TaskManager;
import model.Epic;
import model.SubTask;
import server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class checkEpicHandler {
    protected static TaskManager taskManager;

    public static void main(String[] args) throws IOException, InterruptedException {
        taskManager = Managers.getDefault();
        Epic epic1 = new Epic("title1", "discription1", null, null);
        taskManager.add(epic1);
        SubTask subTask1 = new SubTask("title3", "discription3", epic1.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 3, 13, 15));
        SubTask subTask2 = new SubTask("title4", "discription4", epic1.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 6, 15, 15));
        taskManager.add(subTask1);
        taskManager.add(subTask2);

        Epic epic2 = new Epic("title2", "discription2", null, null);
        taskManager.add(epic2);
        SubTask subTask3 = new SubTask("title5", "discription5", epic2.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 2, 17, 15));
        taskManager.add(subTask3);

        Epic epic4 = new Epic("title1", "discription1", null, null);
        Gson gson = Managers.getJson();
        System.out.println(gson.toJson(epic4));

        System.out.println("-".repeat(100));
        System.out.println("Get запрос на получение SubTasks");
        getEpics(taskManager);

        System.out.println("-".repeat(100));
        System.out.println("Get запрос на получение Epic по id");
        getEpicTaskForid(taskManager, epic1.getId());

        System.out.println("-".repeat(100));
        System.out.println("Удаление Epic по id");
        deleteEpics(taskManager, epic2.getId());
    }

    private static void getEpics(TaskManager taskManager) throws IOException, InterruptedException {
        URI epic_url = URI.create("http://localhost:8080/epics");
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(epic_url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Код - " + response.statusCode());
        System.out.println(response.body());

        taskServer.stop();
    }

    private static void getEpicTaskForid(TaskManager taskManager, int id) throws IOException, InterruptedException {
        URI epic_url = URI.create("http://localhost:8080/epics/" + id);
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(epic_url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Код - " + response.statusCode());
        System.out.println(response.body());
        taskServer.stop();
    }

    private static void deleteEpics(TaskManager taskManager, int id) throws IOException, InterruptedException {
        Gson gson = Managers.getJson();
        URI epic_url = URI.create("http://localhost:8080/epics/" + id);
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(epic_url)
                .GET()
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Код - " + response.statusCode());
        System.out.println(response.body());
        System.out.println("Оставшиеся SubTask taskManager: " + gson.toJson(taskManager.getSubTasks()));
        taskServer.stop();
    }
}
