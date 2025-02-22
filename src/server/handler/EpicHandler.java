package server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import controlles.TaskManager;
import model.Epic;
import model.SubTask;

import java.io.IOException;
import java.util.ArrayList;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    protected void handlerGetEpic(HttpExchange exchange) throws IOException {
        String[] url = exchange.getRequestURI().getPath().split("/");
        if (url.length == 2) {
            response = gson.toJson(taskManager.getEpics());
            sendText(exchange, response, 200);
        } else if (url.length == 3) {
            try {
                int id = Integer.parseInt(url[2]);
                Epic epic = taskManager.getEpicsById(id);
                if (epic == null) {
                    sendNotFound(exchange);
                } else {
                    response = gson.toJson(epic);
                    sendText(exchange, response, 200);
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                sendNotFound(exchange);
            }
        } else {
            try {
                int id = Integer.parseInt(url[2]);
                Epic epic = taskManager.getEpicsById(id);
                if (epic == null) {
                    sendNotFound(exchange);
                } else {
                    ArrayList<SubTask> subtask = taskManager.getEpicsSupTask(epic);
                    response = gson.toJson(subtask);
                    sendText(exchange, response, 200);
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                sendNotFound(exchange);
            }
        }
        System.out.println(getText(exchange));
    }

    private void handlerPost(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        System.out.println(body);
        if (body.isBlank()) {
            sendNotFound(exchange);
            return;
        }
        JsonElement element = JsonParser.parseString(body);
        if (!element.isJsonObject()) {
            sendHasInteractions(exchange);
            return;
        }
        try {
            JsonObject object = element.getAsJsonObject();
            Epic newEpic = gson.fromJson(object, Epic.class);
            taskManager.add(newEpic);
            sendText(exchange, "Successfully", 201);
        } catch (JsonSyntaxException ex) {
            sendHasInteractions(exchange);
        }
    }

    protected void handlerDeleteEpicById(HttpExchange exchange) throws IOException {
        String[] url = exchange.getRequestURI().getPath().split("/");
        try {
            int id = Integer.parseInt(url[2]);
            if (taskManager.getEpicsById(id) == null) {
                sendNotFound(exchange);
            } else {
                System.out.println(taskManager.getEpicsById(id).toString());
                taskManager.deleteEpic(id);
                response = gson.toJson("Successfully");
                System.out.println(response);
                sendText(exchange, response, 200);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
            sendNotFound(exchange);
        } catch (NullPointerException ex) {
            System.out.println("упс пустое значение");
            sendNotFound(exchange);
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handlerGetEpic(exchange);
                break;
            case "DELETE":
                handlerDeleteEpicById(exchange);
                break;
            case "POST":
                handlerPost(exchange);
                break;
        }
        //exchange.close();
    }
}
