package service;

import model.Epic;
import model.StatusOfTask;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class HistoryManagerTest {

    HistoryManager historyManager = new InMemoryHistoryManager();
    Task task;
    Epic epic;
    Subtask subtask1, subtask2;


    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Задача", "", 1);
        epic = new Epic("Эпик", "", 2);
        subtask1 = new Subtask("Подзадача", "", StatusOfTask.NEW, epic);
        subtask2 = new Subtask("Подзадача", "", StatusOfTask.NEW, epic);
    }

    @Test
    void addTaskToHistoryTest() {
        Task task1 = new Task("T1", "", 1);
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(List.of(task1), history, "История пустая");
        assertEquals(1, task1.getId(), "История сохранена неверно");
    }

    @Test
    void maxAttemptsToHistoryTest() {
        Task task1 = new Task("T1", "", 1);
        int i = 0;
        while (i < 20) {
            historyManager.add(task1);;
            i++;
        }
        List<Task> history = historyManager.getHistory();
        assertEquals(List.of(task1), history, "Число записей в истории должно быть не более 1");
    }

    @Test
    void getHistoryTest() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertTrue(history.isEmpty(), "История не пустая");

        historyManager.add(task);
        history = historyManager.getHistory();
        assertEquals(List.of(task), history, "История не сохранена");

        historyManager.remove(1);
        history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История не пустая");

        historyManager.add(task);
        historyManager.add(task);
        history = historyManager.getHistory();
        assertEquals(List.of(task), history, "История сохранена некорректно");
        assertEquals(1, task.getId(), "История сохранена некорректно");
    }

    @Test
    void removeTasksTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask1);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(List.of(task, epic, subtask1), history, "История сохранена некорректно");
        //удаление из начала истории
        historyManager.remove(1);
        history = historyManager.getHistory();
        assertEquals(List.of(epic, subtask1), history, "История сохранена некорректно");
        assertEquals(2, history.get(0).getId(), "История сохранена некорректно");
        assertEquals(0, history.get(1).getId(), "История сохранена некорректно");
        //удаление из середины истории
        historyManager.add(subtask2);
        historyManager.remove(3);
        history = historyManager.getHistory();
        assertEquals(List.of(epic, subtask2), history, "История сохранена некорректно");
        assertEquals(2, history.get(0).getId(), "История сохранена некорректно");
        assertEquals(0, history.get(1).getId(), "История сохранена некорректно");
        //удаление с конца истории
        historyManager.remove(4);
        history = historyManager.getHistory();
        assertEquals(List.of(epic, subtask2), history, "История сохранена некорректно");
        assertEquals(2, history.get(0).getId(), "История сохранена некорректно");
    }

}