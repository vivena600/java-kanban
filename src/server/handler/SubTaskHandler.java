package server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import controlles.TaskManager;
import model.SubTask;

import java.io.IOException;
import java.io.InputStream;

public class SubTaskHandler extends BaseHttpHandler {

    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    private void handlerGetSubTask(HttpExchange exchange) throws IOException {
        String[] url = exchange.getRequestURI().getPath().split("/");
        if (url.length == 2) {
            response = gson.toJson(taskManager.getSubTasks());
            sendText(exchange, response, 200);
        } else {
            try {
                int id = Integer.parseInt(url[2]);
                SubTask subTask = taskManager.getSubTaskById(id);
                if (subTask == null) {
                    sendNotFound(exchange);
                } else {
                    response = gson.toJson(subTask);
                    sendText(exchange, response, 200);
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                sendNotFound(exchange);
            }
        }
        System.out.println(getText(exchange));
    }

    private void handlerDeleteSubTaskById(HttpExchange exchange) throws IOException {
        String[] url = exchange.getRequestURI().getPath().split("/");
        try {
            int id = Integer.parseInt(url[2]);
            if (taskManager.getSubTaskById(id) == null) {
                sendNotFound(exchange);
            } else {
                taskManager.deleteTask(id);
                response = gson.toJson(taskManager.getSubTasks());
                sendText(exchange, response, 200);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
            sendNotFound(exchange);
        }
    }

    private void handlerPost(HttpExchange exchange) throws IOException {
        String[] url = exchange.getRequestURI().getPath().split("/");
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        JsonElement element = JsonParser.parseString(body);
        if (!element.isJsonObject()) {
            sendHasInteractions(exchange);
            return;
        }
        try {
            JsonObject object = element.getAsJsonObject();
            SubTask newSubTask = gson.fromJson(object, SubTask.class);
            if (url.length == 2) {
                if (!taskManager.validatorTime(newSubTask)) {
                    response = gson.toJson("Not Acceptable");
                    sendText(exchange, response, 406);
                }
                taskManager.add(newSubTask);
                sendText(exchange, "Successfully", 201);
            } else {
                int id = Integer.parseInt(url[2]);
                if (taskManager.getSubTaskById(id) == null) {
                    sendNotFound(exchange);
                }
                if (!taskManager.validatorTime(newSubTask)) {
                    response = gson.toJson("Not Acceptable");
                    sendText(exchange, response, 406);
                }
                taskManager.update(newSubTask);
                sendText(exchange, "Successfully", 201);
            }
        } catch (JsonSyntaxException ex) {
            sendHasInteractions(exchange);
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handlerGetSubTask(exchange);
                break;
            case "DELETE":
                handlerDeleteSubTaskById(exchange);
                break;
            case "POST":
                handlerPost(exchange);
                break;
        }
        //exchange.close();
    }
}
