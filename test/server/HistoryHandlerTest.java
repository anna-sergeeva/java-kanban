package server;

import com.google.gson.Gson;
import service.InMemoryTaskManager;
import service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Epic;
import model.Task;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryHandlerTest {
    private TaskManager manager;
    private HttpTaskServer server;
    private Task task;
    private Epic epic;
    private Gson gson = HttpTaskServer.getGson();


    @BeforeEach
    public void setUP() throws IOException {
        manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        server.start();
        task = new Task("Задача", "Задача", Duration.ofMinutes(5), LocalDateTime.now());
        epic = new Epic("Эпик", "Эпик");
        manager.addNewTask(task);
        manager.addNewEpic(epic);
    }

    @AfterEach
    public void close() throws IOException {
        server.stop();
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        manager.getEpicById(epic.getId());
        manager.getTaskById(task.getId());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        List<Task> receivedTasks = gson.fromJson(responseBody, new TaskListTypeToken().getType());

        assertEquals(200, response.statusCode(), "Обработчик истории запросов (HistoryHandler) вернул неуспешный код ответа");
        assertEquals(2, receivedTasks.size(), "Обработчик истории запросов (HistoryHandler) вернул некорректную историю запросов");
    }
}
