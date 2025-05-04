package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {    //класс содержит общие методы для чтения и отправки данных

    //во входные параметры добавлен httpCode, чтобы не писать отдельные функции под другие коды ответов (404, 406, 201)
    protected void sendText(HttpExchange h, String text, int httpCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(httpCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .setPrettyPrinting()
            .create();


}
