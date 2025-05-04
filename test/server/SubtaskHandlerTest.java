package server;

import com.google.gson.*;
import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskHandlerTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();
    Epic epic = new Epic("Эпик", "Эпик");
    Subtask subtask = new Subtask("Подзадача", "Подзадача", epic.getId(), Duration.ofMinutes(5),
                LocalDateTime.now());

    public SubtaskHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
        server.start();
    }

    @AfterEach
    public void shutDown() throws IOException {
        server.stop();
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        final int epicId = manager.addNewEpic(epic);
        subtask = new Subtask("Подзадача", "Подзадача", epic.getId(), Duration.ofMinutes(5),
                LocalDateTime.now());
        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> tasksFromManager = manager.getListOfSubtasks();

        assertNotNull(tasksFromManager, "Метод не вернул подзадачи");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals("Подзадача", tasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        final int epicId = manager.addNewEpic(epic);
        subtask = new Subtask("Подзадача", "Подзадача", epic.getId(), Duration.ofMinutes(5),
                LocalDateTime.now());
        manager.addNewSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray(),"Ответ от сервера не соответствует ожидаемому");
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> tasks = gson.fromJson(jsonArray, new TaskListTypeToken().getType());

        assertNotNull(tasks, "Метод не вернул подзадачи");
        assertEquals(1, tasks.size(), "Некорректное количество задач");
        assertEquals("Подзадача", tasks.get(0).getName(), "Некорректное имя подзадачи");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        final int epicId = manager.addNewEpic(epic);
        subtask = new Subtask("Подзадача", "Подзадача", epic.getId(), Duration.ofMinutes(5),
                LocalDateTime.now());
        final int taskId = manager.addNewSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/subtasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject(),"Ответ от сервера не соответствует ожидаемому");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Task responseTask = gson.fromJson(jsonObject, Subtask.class);

        assertNotNull(responseTask, "Метод не возвращает подзадачи");
        assertEquals("Подзадача", responseTask.getName(), "Некорректное имя подзадачи");
    }

    @Test
    public void deleteTaskById() throws IOException, InterruptedException {
        final int epicId = manager.addNewEpic(epic);
        subtask = new Subtask("Подзадача", "Подзадача", epic.getId(), Duration.ofMinutes(5),
                LocalDateTime.now());
        final int taskId = manager.addNewSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/subtasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(0, manager.getListOfSubtasks().size(), "Задача не удалена");
    }
}