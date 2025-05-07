package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.Managers;
import service.TaskManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    //класс содержит общие методы для чтения и отправки данных

    protected String response;
    protected Gson gson = Managers.getGson();
    protected TaskManager taskManager;
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    //во входные параметры добавлен httpCode, чтобы не писать отдельные функции под другие коды ответов (404, 406, 201)
    protected void sendText(HttpExchange httpExchange, String response, int httpCode) throws IOException {
        httpExchange.sendResponseHeaders(httpCode, 0);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        httpExchange.close();
    }
}
