package server;

import com.sun.net.httpserver.HttpServer;
import controlles.TaskManager;
import server.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    //класс для запуска программы
    private static final int PORT = 8080;
    private HttpServer server;

    protected TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
         this.taskManager = taskManager;
         server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
         server.createContext("/tasks", new TaskHandler(taskManager));
         server.createContext("/subTasks", new SubTaskHandler(taskManager));
         server.createContext("/epics", new EpicHandler(taskManager));
         server.createContext("/prioritized", new PrioritizedHandler(taskManager));
         server.createContext("/history", new HistoryHandler(taskManager));
    }

    //служебные методы
    public void start() {
        System.out.println("Startted HttpTaskServer " + PORT);
        System.out.println("http://localhost:" + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Stopped HttpTaskServer");
    }
}
