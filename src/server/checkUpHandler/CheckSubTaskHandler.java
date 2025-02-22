package server.checkUpHandler;

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

public class CheckSubTaskHandler {
    protected static TaskManager taskManager;

    public static void main(String[] args) throws IOException, InterruptedException {
        taskManager = Managers.getDefault();
        Epic epic = new Epic("title3", "discription3", null, null);
        taskManager.add(epic);
        SubTask subTask1 = new SubTask("title3", "discription3", epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 3, 13, 15));
        SubTask subTask2 = new SubTask("title4", "discription4", epic.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 6, 15, 15));
        taskManager.add(subTask1);
        taskManager.add(subTask2);

        System.out.println("Get tasks");
        System.out.println("-".repeat(100));
        getSubTasksHandler(taskManager);

        System.out.println("Get tasks bi id");
        System.out.println("-".repeat(100));
        getSubTaskForid(taskManager, subTask1.getId());
    }

    protected static void getSubTasksHandler(TaskManager taskManager) throws IOException, InterruptedException {
        URI subTask_url = URI.create("http://localhost:8080/subTasks");
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(subTask_url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Код - " + response.statusCode());
        System.out.println(response.body());

        taskServer.stop();
    }

    private static void getSubTaskForid(TaskManager taskManager, int id) throws IOException, InterruptedException {
        URI subTask_url = URI.create("http://localhost:8080/subTasks/" + id);
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(subTask_url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Код - " + response.statusCode());
        System.out.println(response.body());
        taskServer.stop();
    }
}
