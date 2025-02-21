package server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
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
        System.out.println("тело - " + body);
        JsonElement element = JsonParser.parseString(body);
        if (!element.isJsonObject()) {
            sendHasInteractions(exchange);
            return;
        }
        try {
            JsonObject object = element.getAsJsonObject();
            System.out.println(object.toString());
            //String request = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Task newTask = gson.fromJson(object, Task.class);
            System.out.println(newTask.toString());
        } catch (JsonSyntaxException ex) {
            sendHasInteractions(exchange);
        }

        /*if (request.isBlank()) {
            sendNotFound(exchange);
            return;
        }

        try {
            Task newTask = gson.fromJson(request, Task.class);
            System.out.println(newTask.toString());
            String[] url = exchange.getRequestURI().getPath().split("/");
            if (url.length == 2) {
                taskManager.add(newTask);
                if (taskManager.getTaskByid(newTask.getId()) != newTask) {
                    response = gson.toJson("Not Acceptable");
                    sendText(exchange, response, 406);
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
        } catch (JsonSyntaxException ex) {
            sendHasInteractions(exchange);
        }

         */
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
