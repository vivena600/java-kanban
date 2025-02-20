package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controlles.HistoryManager;
import controlles.InMemoryTaskManager;
import controlles.Managers;
import controlles.TaskManager;
import model.Task;
import server.handler.TaskHandler;

import javax.sound.sampled.Port;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    //класс для запуска программы
    private final int PORT = 8080;
    private HttpServer server;

    private TaskManager taskManager;
    private HistoryManager historyManager;
    private TaskHandler taskHandler;

    public HttpTaskServer(InMemoryTaskManager taskManager) throws IOException {
         this.taskManager = taskManager;
         server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
         server.createContext("/tasks", new TaskHandler(taskManager));
    }

    //служебные методы
    public void start() {
        System.out.println("Startted HttpTaskServer " + PORT);
        System.out.println("http://localhost:" + PORT );
        server.start();
    }
}
