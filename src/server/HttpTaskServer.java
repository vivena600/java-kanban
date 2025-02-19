package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controlles.HistoryManager;
import controlles.InMemoryTaskManager;
import controlles.Managers;
import controlles.TaskManager;

import javax.sound.sampled.Port;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    //класс для запуска программы
    private final int PORT = 8080;
    private HttpServer server;

    private TaskManager taskManager;
    private HistoryManager historyManager;

    public HttpTaskServer() {
        this.taskManager = (Managers.getDefault());
    }

    public HttpTaskServer(InMemoryTaskManager taskManager) throws IOException {
         this.taskManager = taskManager;
         server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
         server.createContext("tasks", new TaskHandler(taskManager));
    }

    private void handleGetTasks(HttpExchange httpExchange) {
    }
    /*

    public static void main(String[] args) {
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();
    }

     */

    //служебные методы
    public void start() {
        System.out.println("Startted HttpTaskServer " + PORT);
        System.out.println("http://locationhost:" + PORT );
        server.start();
    }
}
