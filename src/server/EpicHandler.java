package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    EpicHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");
        if (requestMethod.equals("GET") && pathParts.length == 2 && pathParts[1].equals("epics")) {
            getTasksHandle(exchange, gson);
        } else if (requestMethod.equals("GET") && pathParts.length == 3 && pathParts[1].equals("epics")) {
            getTaskHandle(exchange, gson, pathParts);
        } else if (requestMethod.equals("GET") && pathParts.length == 4 && pathParts[1].equals("epics") && pathParts[3].equals("subtasks")) {
            getSubtasksHandle(exchange, gson, pathParts);
        } else if (requestMethod.equals("POST") && pathParts.length == 2 && pathParts[1].equals("epics")) {
            postTaskHandle(exchange, gson);
        } else if (requestMethod.equals("DELETE") && pathParts.length == 3 && pathParts[1].equals("epics")) {
            deleteTaskHandle(exchange, pathParts);
        } else {
            sendText(exchange, "Метод не найден", 404);
        }
    }

    private void getTasksHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            List<Epic> tasks = taskManager.getListOfEpics();
            String text = gson.toJson(tasks);
            sendText(exchange, text, 200);
        } catch (Exception exp) {
            sendText(exchange, "Произошла ошибка при обработке запроса" + exp.getMessage(), 500);
        }
    }
    

    private void getTaskHandle(HttpExchange exchange, Gson gson, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Epic task = taskManager.getEpicById(id);
            sendText(exchange, gson.toJson(task), 200);
        } catch (Exception e) {
            sendText(exchange, "Эпик с идентификатором " + pathParts[2] + " не найден", 404);
        }
    }

    private void postTaskHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            InputStream bodyInputStream = exchange.getRequestBody();
            String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic taskDeserialized = gson.fromJson(body, Epic.class);
            if (taskDeserialized == null) {
                sendText(exchange, "Произошла ошибка при обработке запроса, тело запроса не преобразовано в эпик", 500);
                return;
            }
            if (taskDeserialized.getId() == null || taskDeserialized.getId() == 0) {
                taskManager.addNewEpic(taskDeserialized);
                sendText(exchange, "Запрос выполнен успешно", 201);
            } else {
                if (taskManager.getEpicById(taskDeserialized.getId()) == null) {
                    sendText(exchange, "Не найден эпик с идентификатором " + taskDeserialized.getId(), 404);
                } else {
                    taskManager.updateEpic(taskDeserialized);
                    sendText(exchange, "Запрос выполнен успешно", 201);
                }
            }
        } catch (Exception exp) {
            sendText(exchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 500);
        }
    }

    private void deleteTaskHandle(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Epic task = taskManager.getEpicById(id);
            taskManager.removeEpicById(task.getId());
            sendText(exchange, "Эпик успешно удален", 200);
        } catch (Exception e) {
            sendText(exchange, "Эпик с идентификатором " + pathParts[2] + " не найден", 404);
        }
    }
}