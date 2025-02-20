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

public class checkTaskHandler {
    private static InMemoryTaskManager taskManager;

    public static void main(String[] args) throws IOException, InterruptedException {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        Task task2 = new Task("Задача 2", "Описание 2",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 01, 04, 00, 00));
        Task task1 = new Task("Задача 1", "Описание 1", Duration.ofSeconds(50), null);
        Task task3 = new Task("Задача 2", "Описание 2",  Duration.ofMinutes(35),
                LocalDateTime.of(2026, 02, 04, 00, 00)); //id = 2
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(task3);

        Gson gson = Managers.getJson();
        System.out.println(gson.toJson(task2));
        /*
        URI task_url = URI.create("http://localhost:8080/tasks");
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(task_url)
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println();

         */
    }
}
