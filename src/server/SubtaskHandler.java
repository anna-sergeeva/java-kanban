package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import service.TaskManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");
        if (requestMethod.equals("GET") && pathParts.length == 2 && pathParts[1].equals("subtasks")) {
            getSubtasksHandle(exchange, gson);
        } else if (requestMethod.equals("GET") && pathParts.length == 3 && pathParts[1].equals("subtasks")) {
            getSubtaskHandle(exchange, gson, pathParts);
        } else if (requestMethod.equals("POST") && pathParts.length == 2 && pathParts[1].equals("subtasks")) {
            postSubtaskHandle(exchange, gson);
        } else if (requestMethod.equals("DELETE") && pathParts.length == 3 && pathParts[1].equals("subtasks")) {
            deleteSubtaskHandle(exchange, pathParts);
        } else {
            sendText(exchange, "Метод не найден", 404);
        }
    }

    private void getSubtasksHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            List<Subtask> tasks = taskManager.getListOfSubtasks();
            String text = gson.toJson(tasks);
            sendText(exchange, text, 200);
        } catch (Exception exp) {
            sendText(exchange, "Произошла ошибка при обработке запроса" + exp.getMessage(), 500);
        }
    }

    private void getSubtaskHandle(HttpExchange exchange, Gson gson, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Subtask task = taskManager.getSubtaskById(id);
            sendText(exchange, gson.toJson(task), 200);
        } catch (Exception e) {
            sendText(exchange, "Подзадача с идентификатором " + pathParts[2] + " не найдена", 404);
        }
    }

    private void postSubtaskHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            InputStream bodyInputStream = exchange.getRequestBody();
            String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtaskDeserialized = gson.fromJson(body, Subtask.class);
            if (subtaskDeserialized == null) {
                sendText(exchange, "Не удалось преобразовать тело запроса в задачу!", 404);
            } else if (subtaskDeserialized.getId() != null) {
                taskManager.updateSubtask(subtaskDeserialized);
                sendText(exchange, "Запрос выполнен успешно: подзадача обновлена", 201);
            } else {
                taskManager.addNewSubtask(subtaskDeserialized);
                sendText(exchange, "Запрос выполнен успешно: подзадача добавлена", 201);
            }
        } catch (Exception exp) {
            sendText(exchange, "Произошла ошибка при обработке запроса" + exp.getMessage(), 500);
        }
    }

    private void deleteSubtaskHandle(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            taskManager.removeSubtaskById(Integer.parseInt(pathParts[2]));
            sendText(exchange, "Запрос выполнен успен=шно: подзадача удалена", 200);
        } catch (Exception e) {
            sendText(exchange, "Подзадача с идентификатором " + pathParts[2] + " не найдена", 404);
        }
    }
}