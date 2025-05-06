package server;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;
import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");
        if (requestMethod.equals("GET") && pathParts.length == 2 && pathParts[1].equals("prioritized")) {
            try {
                List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                String text = gson.toJson(prioritizedTasks);
                sendText(exchange, text, 200);
            } catch (Exception exp) {
                sendText(exchange, "Произошла ошибка при обработке запроса " + exp.getMessage(), 500);
            }
        } else {
            sendText(exchange, "Метод не найден", 404);
        }
    }
}
