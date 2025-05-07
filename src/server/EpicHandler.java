package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.TaskManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");
        if (requestMethod.equals("GET") && pathParts.length == 2 && pathParts[1].equals("epics")) {
            getEpicsHandle(exchange, gson);
        } else if (requestMethod.equals("GET") && pathParts.length == 3 && pathParts[1].equals("epics")) {
            getEpicHandle(exchange, gson, pathParts);
        } else if (requestMethod.equals("POST") && pathParts.length == 2 && pathParts[1].equals("epics")) {
            postEpicHandle(exchange, gson);
        } else if (requestMethod.equals("DELETE") && pathParts.length == 3 && pathParts[1].equals("epics")) {
            deleteEpicHandle(exchange, pathParts);
        } else {
            sendText(exchange, "Метод не найден", 404);
        }
    }

    private void getEpicsHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            List<Epic> tasks = taskManager.getListOfEpics();
            String text = gson.toJson(tasks);
            sendText(exchange, text, 200);
        } catch (Exception exp) {
            sendText(exchange, "Произошла ошибка при обработке запроса" + exp.getMessage(), 500);
        }
    }


    private void getEpicHandle(HttpExchange exchange, Gson gson, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Epic task = taskManager.getEpicById(id);
            sendText(exchange, gson.toJson(task), 200);
        } catch (Exception e) {
            sendText(exchange, "Эпик с идентификатором " + pathParts[2] + " не найден", 404);
        }
    }

    private void postEpicHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            InputStream bodyInputStream = exchange.getRequestBody();
            String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic epicDeserialized = gson.fromJson(body, Epic.class);
            if (epicDeserialized == null) {
                sendText(exchange, "Произошла ошибка при обработке запроса: тело запроса не преобразовано в эпик", 404);
            } else if (epicDeserialized.getId() != null) {
                taskManager.updateEpic(epicDeserialized);
                sendText(exchange, "Запрос выполнен успешно: эпик обновлен", 201);
            } else {
                taskManager.addNewEpic(epicDeserialized);
                sendText(exchange, "Запрос выполнен успешно: эпик добавлен", 201);
            }
        } catch (Exception exp) {
            sendText(exchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 500);
        }
    }

    private void deleteEpicHandle(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            taskManager.removeEpicById(Integer.parseInt(pathParts[2]));
            sendText(exchange, "Запрос выполнен успешно: эпик удален", 200);
        } catch (Exception e) {
            sendText(exchange, "Эпик с идентификатором " + pathParts[2] + " не найден", 404);
        }
    }
}