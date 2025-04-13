package test.service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;



class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    File file;

    @BeforeEach
    public void initFileManager() throws IOException {
        file = new File("./resources/task.csv");
        taskManager = new FileBackedTaskManager(file);
        task = new Task("Задача", "Задача", Duration.ofMinutes(10), LocalDateTime.now());
        taskManager.addNewTask(task);
        epic = new Epic("Эпик", "Эпик");
        taskManager.addNewEpic(epic);
        subtask = new Subtask("Подзадача", "Подзадача", epic.getId(), Duration.ofMinutes(10),
                LocalDateTime.now().minusMinutes(15L));
        taskManager.addNewSubtask(subtask);
    }

    @Test
    void loadFromEmptyFile() throws IOException {
        File file = File.createTempFile("testFile-", ".csv");
        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);
        assertNotNull(taskManager, "taskManager принимает null");
        assertEquals(taskManager.getListOfTasks().size(), 0, "Количество задач не равно 0");
        assertEquals(taskManager.getHistory().size(), 0, "Количество задач в истории не равно 0");
    }

    @Test
    public void testException() {
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager taskManager = new FileBackedTaskManager(new File("/invalid/path/task.csv"));
            taskManager.save();
        }, "Попытка сохранить файл приводит к ошибке");
    }

    @Test
    void saveToEmptyFile() throws IOException {
        File file = File.createTempFile("testEmptyFile-", ".csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        taskManager.save();
        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        assertNotNull(taskManagerFromFile, "taskManagerFromFile принимает null!");
        assertEquals(taskManagerFromFile.getListOfTasks().size(), 0, "Количество задач не равно 0");
        assertEquals(taskManagerFromFile.getHistory().size(), 0, "Количество задач в истории не равно 0");
    }

    @Test
    void saveLoadFile() throws IOException {
        File file = File.createTempFile("testEmptyFile-", ".csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        Task task = new Task("Задача", "Задача", Duration.ofMinutes(5), LocalDateTime.now());
        final int taskId = taskManager.addNewTask(task);
        //вызовем получение задачи для обновления истории
        taskManager.getTaskById(taskId);

        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        assertNotNull(taskManagerFromFile, "taskManagerFromFile принимает null!");
        assertEquals(taskManagerFromFile.getListOfTasks().size(), taskManager.getListOfTasks().size(), "Количество задач в менеджерах не равно");
        assertEquals(taskManagerFromFile.getHistory().size(), taskManagerFromFile.getHistory().size(), "Количество задач в истории менеджеров не равно");
    }
}