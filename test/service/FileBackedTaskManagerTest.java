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
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
    }

    @Test
    void loadFromFileTest() {
        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, fileManager.tasks.size(), "Количество задач после выгрузки не совпадает");
        assertEquals(taskManager.getListOfTasks(), fileManager.getListOfTasks(),
                "Список задач после выгрузки не совпададает");
        assertEquals(1, fileManager.epics.size(), "Количество эпиков после выгрузки не совпадает");
        assertEquals(taskManager.getListOfEpics(), fileManager.getListOfEpics(),
                "Список эпиков после выгрузки не совпадает");
        assertEquals(1, fileManager.subtasks.size(),
                "Количество подзадач после выгрузки не совпадает");
        assertEquals(taskManager.getListOfTasks(), fileManager.getListOfTasks(),
                "Список подзадач после выгрузки не совпадает");
        assertNotNull(taskManager, "taskManager принимает null");
        assertEquals(taskManager.getListOfTasks().size(), 1, "Количество задач не равно 0");
        assertEquals(taskManager.getHistory().size(), 3, "Количество задач в истории не равно 0");
    }


    @Test
    void saveToEmptyFile() throws IOException {
        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        assertNotNull(taskManagerFromFile, "taskManagerFromFile принимает null!");
        assertEquals(taskManagerFromFile.getListOfTasks().size(), 1, "Количество задач равно 0");
        assertEquals(taskManagerFromFile.getHistory().size(), 0, "Количество задач в истории не равно 0");
    }

    @Test
    void saveLoadFile() throws IOException {
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        assertNotNull(taskManagerFromFile, "taskManagerFromFile принимает null!");
        assertEquals(taskManagerFromFile.getListOfTasks().size(), taskManager.getListOfTasks().size(), "Количество задач в менеджерах не равно");
        assertEquals(taskManagerFromFile.getHistory().size(), taskManagerFromFile.getHistory().size(), "Количество задач в истории менеджеров не равно");
        assertEquals(taskManagerFromFile.getListOfTasks(), taskManager.getListOfTasks(), "Задачи в менеджерах отличаются");
        assertEquals(taskManagerFromFile.getListOfEpics(), taskManager.getListOfEpics(), "Задачи в менеджерах отличаются");
        assertEquals(taskManagerFromFile.getListOfSubtasks(), taskManager.getListOfSubtasks(), "Задачи в менеджерах отличаются");
    }
}