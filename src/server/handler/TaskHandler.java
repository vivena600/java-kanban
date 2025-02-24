package server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import controlles.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    private void handlerGetTask(HttpExchange exchange) throws IOException {
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

    private void handlerPost(HttpExchange exchange) throws IOException {
        String[] url = exchange.getRequestURI().getPath().split("/");
        String body = getText(exchange);
        JsonElement element = JsonParser.parseString(body);
        if (!element.isJsonObject()) {
            sendHasInteractions(exchange);
            return;
        }

        try {
            final Task newTask = gson.fromJson(body, Task.class);
            if (url.length == 2) {
                if (!taskManager.validatorTime(newTask)) {
                    response = gson.toJson("Not Acceptable");
                    sendText(exchange, response, 406);
                }
                taskManager.add(newTask);
                sendText(exchange, "Successfully", 201);
            } else {
                int id = Integer.parseInt(url[2]);
                if (taskManager.getTaskByid(id) == null) {
                    sendNotFound(exchange);
                }
                if (!taskManager.validatorTime(newTask)) {
                    response = gson.toJson("Not Acceptable");
                    sendText(exchange, response, 406);
                }
                taskManager.update(newTask);
                sendText(exchange, "Successfully", 201);
                }
        } catch (JsonSyntaxException ex) {
            sendHasInteractions(exchange);
        }
    }

    private void handlerDeleteTasksById(HttpExchange exchange) throws IOException {
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
            default:
                sendNotFound(exchange);
        }
        exchange.close();
    }
}
