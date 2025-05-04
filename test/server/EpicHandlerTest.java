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

public class EpicHandlerTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public EpicHandlerTest() throws IOException {
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
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Эпик");
        String taskJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> tasksFromManager = manager.getListOfEpics();

        assertNotNull(tasksFromManager, "Метод не вернул эпики");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Эпик", tasksFromManager.get(0).getName(), "Некорректное имя эпика");
    }

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Эпик");
        manager.addNewEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray(), "Ответ от сервера не соответствует ожидаемому");
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> tasks = gson.fromJson(jsonArray, new TaskListTypeToken().getType());

        //проверяем, что вернулась одна задача с корректным именем
        assertNotNull(tasks, "Метод не вернул эпики");
        assertEquals(1, tasks.size(), "Некорректное количество эпиков");
        assertEquals("Эпик", tasks.get(0).getName(), "Некорректное имя эпика");
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {

        Epic epic = new Epic("Эпик", "Testing task 2");
        final int epicId = manager.addNewEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject(), "Ответ от сервера не соответствует ожидаемому.");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Epic responseTask = gson.fromJson(jsonObject, Epic.class);

        assertNotNull(responseTask, "Метод не вернул эпики");
        assertEquals("Эпик", responseTask.getName(), "Некорректное имя эпика");
    }

    @Test
    public void deleteEpicById() throws IOException, InterruptedException {

        Epic epic = new Epic("Эпик", "Эпик");
        final int epicId = manager.addNewEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getListOfEpics().size(), "Эпик не удален");
    }

    @Test
    public void testGetEpicsSubtask() throws IOException, InterruptedException {

        Epic epic = new Epic("Эпик", "Эпик");
        final int epicId = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("Подзадача", "Подзадача", epic.getId(), Duration.ofMinutes(5),
                LocalDateTime.now());
        final int subtaskId = manager.addNewSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId + "/subtasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

    }
}