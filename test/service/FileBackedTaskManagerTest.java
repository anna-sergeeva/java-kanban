package test.service;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.Test;
import model.*;
import service.FileBackedTaskManager;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    File file;
    FileBackedTaskManager fileBackedTaskManager;

    @Test
    public void loadAndSaveTest() throws ManagerSaveException, IOException {
        file = File.createTempFile("testEmptyFile-", ".csv");
        fileBackedTaskManager = new FileBackedTaskManager(file);
        Task task = new Task(1, "Задача 1", "Задача 1");
        Epic epic = new Epic(2, "Эпик 1", "Эпик 1");
        Subtask subtask = new Subtask(3, "Подзадача 1", "Подзадача 1", 2);
        fileBackedTaskManager.addNewTask(task);
        fileBackedTaskManager.addNewEpic(epic);
        fileBackedTaskManager.addNewSubtask(subtask);
        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(fileBackedTaskManager.getListOfTasks(), fileManager.getListOfTasks(),
                "Произошла ошибка при записи или чтении файла с задачами");
        assertEquals(fileBackedTaskManager.getListOfEpics(), fileManager.getListOfEpics(),
                "Произошла ошибка при записи или чтении файла с эпиками");
        assertEquals(fileBackedTaskManager.getListOfSubtasks(), fileManager.getListOfSubtasks(),
                "Произошла ошибка при записи или чтении файла с подзадачами");
    }
}