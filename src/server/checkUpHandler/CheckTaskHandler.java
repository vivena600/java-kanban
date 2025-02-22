package server.checkUpHandler;

import com.google.gson.Gson;
import controlles.InMemoryTaskManager;
import controlles.Managers;
import controlles.TaskManager;
import model.Task;
import server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class CheckTaskHandler {
    protected static TaskManager taskManager;

    public static void main(String[] args) throws IOException, InterruptedException {
        taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача 1", "Описание 1",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 1, 3, 0, 0));
        taskManager.add(task1);
        Task task2 = new Task("Задача 2", "Описание 2",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 1, 4, 0, 0));
        taskManager.add(task2);
        System.out.println("Get запрос на получение Task по id");
        getTaskForid(taskManager, 1);

        System.out.println("-".repeat(100));
        System.out.println("Get запрос на получение Tasks");
        getTasks(taskManager);

        System.out.println("-".repeat(100));
        System.out.println("Добавление Task");
        postNewTask(taskManager);

    }

    private static void getTasks(TaskManager taskManager) throws IOException, InterruptedException {
        URI task_url = URI.create("http://localhost:8080/tasks");
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(task_url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Код - " + response.statusCode());
        System.out.println(response.body());

        taskServer.stop();
    }

    private static void getTaskForid(TaskManager taskManager, int id) throws IOException, InterruptedException {
        URI task_url = URI.create("http://localhost:8080/tasks/" + id);
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(task_url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Код - " + response.statusCode());
        System.out.println(response.body());
        taskServer.stop();
    }

    private static void postNewTask(TaskManager taskManager) throws IOException, InterruptedException {
        Task task3 = new Task("Задача 3", "Описание 3",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 1, 5, 6, 0));
        URI task_url = URI.create("http://localhost:8080/tasks");
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = Managers.getJson();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(task3));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(task_url)
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(taskManager.getTasks());

        System.out.println("Код - " + response.statusCode());
        System.out.println(response.body());
        taskServer.stop();
    }
}
