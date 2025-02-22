package server.handler;

import com.sun.net.httpserver.HttpExchange;
import controlles.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    private void handlerGetHistory(HttpExchange exchange) throws IOException {
        response = gson.toJson(taskManager.getHistory());
        sendText(exchange, response, 200);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handlerGetHistory(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
    }
}
