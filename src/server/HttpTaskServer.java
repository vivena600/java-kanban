package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controlles.HistoryManager;
import controlles.InMemoryTaskManager;
import controlles.Managers;
import controlles.TaskManager;
import model.Task;
import model.TaskStatus;
import server.handler.TaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    //класс для запуска программы
    private final int PORT = 8080;
    private HttpServer server;

    private TaskManager taskManager;
    private HistoryManager historyManager;
    private TaskHandler taskHandler;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
         this.taskManager = taskManager;
         System.out.println(taskManager.getTasks().toString());
         server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
         server.createContext("/tasks", new TaskHandler(taskManager));
    }

    //служебные методы
    public void start() {
        System.out.println("Startted HttpTaskServer " + PORT);
        System.out.println("http://localhost:" + PORT );
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Stopped HttpTaskServer");
    }
}
