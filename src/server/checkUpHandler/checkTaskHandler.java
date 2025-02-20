package server.checkUpHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controlles.InMemoryTaskManager;
import controlles.Managers;
import controlles.TaskManager;
import model.Task;
import server.HttpTaskServer;
import server.adapters.DurationAdapters;
import server.adapters.LocalDateTimeAdapters;

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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapters())
                .registerTypeAdapter(Duration.class, new DurationAdapters())
                .create();
        taskManager = new InMemoryTaskManager();
        Task task2 = new Task("Задача 2", "Описание 2",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 01, 04, 00, 00));
        taskManager.add(task2);

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
