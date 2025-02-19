package server;

import com.sun.net.httpserver.HttpExchange;
import controlles.InMemoryTaskManager;
import controlles.TaskManager;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(InMemoryTaskManager taskManager) {
        super(taskManager);
    }

    protected void handlerGetTask(HttpExchange exchange) {
        String[] url = exchange.getRequestURI().getPath().split("/");
        if (url.length == 2) {
            //как-то возвращаем таски в gson
        } else {
            int id = Integer.parseInt(url[2]);
            TaskManager task = (TaskManager) taskManager.getTaskByid(id);
            //дальше как-то разобраться с gson
        }

    }

    protected void getTasksForId() {

    }
}
