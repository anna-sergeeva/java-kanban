package test.service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import service.InMemoryTaskManager;
import java.time.Duration;
import java.time.LocalDateTime;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void initInMemoryManager() {
        taskManager = new InMemoryTaskManager();
        task = new Task("Задача", "Задача", Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addNewTask(task);
        epic = new Epic("Эпик", "Эпик");
        taskManager.addNewEpic(epic);
        subtask = new Subtask("Подзадача", "Подзадача", epic.getId(), Duration.ofMinutes(30),
                LocalDateTime.now().minusMinutes(10L));
        taskManager.addNewSubtask(subtask);
    }

}

