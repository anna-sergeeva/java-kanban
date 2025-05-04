package server;

import com.google.gson.*;
import service.InMemoryTaskManager;
import service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TaskHandlerTest {
    private TaskManager manager;
    private HttpTaskServer server;
    private Task task;
    private Gson gson = HttpTaskServer.getGson();

    @BeforeEach
    public void setUP() throws IOException {
        manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        server.start();
        task = new Task("Задача", "Задача", Duration.ofMinutes(5), LocalDateTime.now());
    }

    @AfterEach
    public void close() throws IOException {
        server.stop();
    }

    @Test
    // НОВОЕ!!!
    public void testGetTasks() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Задача", Duration.ofMinutes(5), LocalDateTime.now());
        manager.addNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray(),"Ответ от сервера не соответствует ожидаемому");
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> tasks = gson.fromJson(jsonArray, new TaskListTypeToken().getType());

        assertNotNull(tasks, "Метод не вернул задачи");
        assertEquals(1, tasks.size(), "Некорректное количество задач");
        assertEquals("Задача", tasks.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {

        Task task = new Task("Задача", "Задача", Duration.ofMinutes(5), LocalDateTime.now());

        final int taskId = manager.addNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject(),"Ответ от сервера не соответствует ожидаемому");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Task responseTask = gson.fromJson(jsonObject, Task.class);

        assertNotNull(responseTask, "Метод не вернул задачи");
        assertEquals("Задача", responseTask.getName(), "Некорректное имя задачи");
    }

    @Test
    public void deleteTaskById() throws IOException, InterruptedException {

        Task task = new Task("Задача", "Задача", Duration.ofMinutes(5), LocalDateTime.now());

        final int taskId = manager.addNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(0, manager.getListOfTasks().size(), "Задача не удалена");
    }
}
