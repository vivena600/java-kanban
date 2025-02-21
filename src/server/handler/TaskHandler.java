package server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import controlles.InMemoryTaskManager;
import controlles.Managers;
import controlles.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TaskHandler extends BaseHttpHandler {
    protected InMemoryTaskManager memoryTaskManager = (InMemoryTaskManager) Managers.getDefault();

    public TaskHandler(TaskManager taskManager) throws IOException {
        super(taskManager);
        System.out.println(taskManager.getTasks().toString());
    }

    protected void handlerGetTask(HttpExchange exchange) throws IOException {
        String[] url = exchange.getRequestURI().getPath().split("/");
        if (url.length == 2) {
            response = gson.toJson(taskManager.getTasks());
            sendText(exchange, response, 200);
        } else {
            try {
                int id = Integer.parseInt(url[2]);
                Task task = taskManager.getTaskByid(id);
                if (task == null) {
                    sendNotFound(exchange);
                } else {
                    response = gson.toJson(task);
                    sendText(exchange, response, 200);
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                sendNotFound(exchange);
            }
        }
        System.out.println(getText(exchange));
    }

    protected void handlerPost(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        JsonElement element = JsonParser.parseString(body);
        if (!element.isJsonObject()) {
            sendHasInteractions(exchange);
            return;
        }
        JsonObject object = element.getAsJsonObject();
        Task newTask = gson.fromJson(object, Task.class);

        String[] url = exchange.getRequestURI().getPath().split("/");
        if (url.length == 2) {
            taskManager.add(newTask);
            if (taskManager.getTaskByid(newTask.getId()) != newTask) {
                response = gson.toJson("Not Acceptable");
                sendText(exchange, response, 406);
            }
        } else {
            int id = Integer.parseInt(url[2]);
            if (taskManager.getTaskByid(id) == null) {
                sendNotFound(exchange);
            }
            taskManager.update(newTask);
            if (taskManager.getTaskByid(newTask.getId()) != newTask) {
                response = gson.toJson("Not Acceptable");
                sendText(exchange, response, 406);
            }
        }
    }

    protected void handlerDeleteTasksById(HttpExchange exchange) throws IOException {
        String[] url = exchange.getRequestURI().getPath().split("/");
        try {
            int id = Integer.parseInt(url[2]);
            if (taskManager.getTaskByid(id) == null) {
                sendNotFound(exchange);
            } else {
                taskManager.deleteTask(id);
                response = gson.toJson(taskManager.getTasks());
                sendText(exchange, response, 200);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
            sendNotFound(exchange);
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handlerGetTask(exchange);
                break;
            case "DELETE":
                handlerDeleteTasksById(exchange);
                break;
            case "POST":
                handlerPost(exchange);
                break;
        }
        //exchange.close();
    }
}

class TaskArrayListTypeToken extends TypeToken<ArrayList<Task>> {

}
