package server.checkUpHandler;

import controlles.InMemoryTaskManager;
import controlles.Managers;
import controlles.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class CheckPrioritizedHandler {

    public static void main(String[] args) throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача 1", "Описание 1",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 1, 3, 0, 0));
        taskManager.add(task1);
        Task task2 = new Task("Задача 2", "Описание 2",  Duration.ofMinutes(35),
                LocalDateTime.of(2025, 1, 4, 0, 0));
        taskManager.add(task2);
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

        URI task_url = URI.create("http://localhost:8080/prioritized");
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
}
