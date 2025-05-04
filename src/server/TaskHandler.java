package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import service.TaskManager;
import model.Task;



public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");
        if (requestMethod.equals("GET") && pathParts.length == 2 && pathParts[1].equals("tasks")) {
            getTasksHandle(exchange, gson);
        } else if (requestMethod.equals("GET") && pathParts.length == 3 && pathParts[1].equals("tasks")) {
            getTaskHandle(exchange, gson, pathParts);
        } else if (requestMethod.equals("POST") && pathParts.length == 2 && pathParts[1].equals("tasks")) {
            postTaskHandle(exchange, gson);
        } else if (requestMethod.equals("DELETE") && pathParts.length == 3 && pathParts[1].equals("tasks")) {
            deleteTaskHandle(exchange, pathParts);
        } else {
            sendText(exchange, "Метод не найден", 404);
        }
    }

    private void getTasksHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            List<Task> tasks = taskManager.getListOfTasks();
            String text = gson.toJson(tasks);
            sendText(exchange, text, 200);
        } catch (Exception exp) {
            sendText(exchange, "Произошла ошибка при обработке запроса " + exp.getMessage(), 500);
        }
    }

    private void getTaskHandle(HttpExchange exchange, Gson gson, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Task task = taskManager.getTaskById(id);
            sendText(exchange, gson.toJson(task), 200);
        } catch (Exception e) {
            sendText(exchange, "Задача с идентификатором " + pathParts[2] + " не найдена", 404);
        }
    }

    private void postTaskHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            InputStream bodyInputStream = exchange.getRequestBody();
            String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task taskDeserialized = gson.fromJson(body, Task.class);
            if (taskDeserialized == null) {
                sendText(exchange, "Произошла ошибка при обработке запроса, тело запроса не преобразовано в задачу", 500);
                return;
            }
            if (taskDeserialized.getId() == null || taskDeserialized.getId() == 0) {
                taskManager.addNewTask(taskDeserialized);
                sendText(exchange, "Запрос выполнен успешно", 201);
            } else {
                if (taskManager.getTaskById(taskDeserialized.getId()) == null) {
                    sendText(exchange, "Не найдена задача с идентификатором " + taskDeserialized.getId(), 404);
                } else {
                    taskManager.updateTask(taskDeserialized);
                    sendText(exchange, "Запрос выполнен успешно", 201);
                }
            }
        } catch (Exception exp) {
            sendText(exchange, "Произошла ошибка при обработке запроса" + exp.getMessage(), 500);
        }
    }

    private void deleteTaskHandle(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Task task = taskManager.getTaskById(id);
            taskManager.removeTaskById(task.getId());
            sendText(exchange, "Задача успешно удалена", 200);
        } catch (Exception e) {
            sendText(exchange, "Задача с идентификатором " + pathParts[2] + " не найдена", 404);
        }
    }
}

