package server.handler;

import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import controlles.InMemoryTaskManager;
import controlles.Managers;
import controlles.TaskManager;
import model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskHandler extends BaseHttpHandler {
    protected InMemoryTaskManager memoryTaskManager = (InMemoryTaskManager) Managers.getDefault();

    public TaskHandler(TaskManager taskManager) throws IOException {
        super(taskManager);
        System.out.println(taskManager.getTasks().toString());
    }

    protected void handlerGetTask(HttpExchange exchange) throws IOException {
        //String[] url = exchange.getRequestURI().getPath().split("/");
        response = gson.toJson(taskManager.getTasks());
        sendText(exchange, response, 200);
        System.out.println(getText(exchange));
        /*
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

         */

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handlerGetTask(exchange);
                break;
        }
        //exchange.close();
    }
}

class TaskArrayListTypeToken extends TypeToken<ArrayList<Task>> {

}
