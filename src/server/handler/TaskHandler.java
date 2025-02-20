package server.handler;

import com.sun.net.httpserver.HttpExchange;
import controlles.InMemoryTaskManager;
import controlles.TaskManager;
import server.HttpTaskServer;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(InMemoryTaskManager taskManager) throws IOException {
        super(taskManager);
    }

    protected void handlerGetTask(HttpExchange exchange) throws IOException {
        String[] url = exchange.getRequestURI().getPath().split("/");
        if (url.length == 2) {
            response = gson.toJson(taskManager.getTasks());
            sendText(exchange, response, 200);
            System.out.println(getText(exchange));
            return;
        } else {
            int id = Integer.parseInt(url[2]);
            //TaskManager task = (TaskManager) taskManager.getTaskByid(id);
            //дальше как-то разобраться с gson
        }

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handlerGetTask(exchange);
                break;
        }
        exchange.close();
    }
}
