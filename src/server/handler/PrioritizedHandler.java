package server.handler;

import com.sun.net.httpserver.HttpExchange;
import controlles.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handlerGetPrioritiz(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
        //exchange.close();
    }

    private void handlerGetPrioritiz(HttpExchange exchange) throws IOException {
        System.out.println(taskManager.getPrioritizedTasks().toString());
        response = gson.toJson(taskManager.getPrioritizedTasks());
        sendText(exchange, response, 200);
    }
}
