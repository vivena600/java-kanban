package server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import controlles.TaskManager;
import model.Epic;
import model.Task;

import java.io.IOException;
import java.io.InputStream;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    protected void handlerGetEpic(HttpExchange exchange) throws IOException {
        String[] url = exchange.getRequestURI().getPath().split("/");
        if (url.length == 2) {
            response = gson.toJson(taskManager.getEpics());
            sendText(exchange, response, 200);
        } else {
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
        }
        System.out.println(getText(exchange));
    }
    /*

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
    */

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
                //handlerPost(exchange);
                break;
        }
        //exchange.close();
    }
}
