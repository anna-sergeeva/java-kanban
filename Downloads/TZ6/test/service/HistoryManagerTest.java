package service;

import model.Epic;
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
        subtask1 = new Subtask("Подзадача", "", 3, 2);
        subtask2 = new Subtask("Подзадача", "", 4, 2);
    }

    @Test
    void addTaskToHistoryTest() {
        Task task1 = new Task("T1", "", 1);
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(1, history.size(), "История пустая");
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
        assertEquals(1, history.size(), "Число записей в истории должно быть не более 1");
    }

    @Test
    void getHistoryTest() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertTrue(history.isEmpty(), "История не пустая");

        historyManager.add(task);
        history = historyManager.getHistory();
        assertEquals(1, history.size(), "История не сохранена");

        historyManager.remove(1);
        history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История не пустая");

        historyManager.add(task);
        historyManager.add(task);
        history = historyManager.getHistory();
        assertEquals(1, history.size(), "История сохранена неверно");
        assertEquals(1, task.getId(), "История сохранена неверно");
    }

    @Test
    void removeTasksTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask1);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(3, history.size(), "История сохранена неверно");
        //удаление из начала истории
        historyManager.remove(1);
        history = historyManager.getHistory();
        assertEquals(2, history.size(), "История сохранена неверно");
        assertEquals(2, history.get(0).getId(), "История сохранена неверно");
        assertEquals(3, history.get(1).getId(), "История сохранена неверно");
        //удаление из середины истории
        historyManager.add(subtask2);
        historyManager.remove(3);
        history = historyManager.getHistory();
        assertEquals(2, history.size(), "История сохранена неверно");
        assertEquals(2, history.get(0).getId(), "История сохранена неверно");
        assertEquals(4, history.get(1).getId(), "История сохранена неверно");
        //удаление с конца истории
        historyManager.remove(4);
        history = historyManager.getHistory();
        assertEquals(1, history.size(), "История сохранена неверно");
        assertEquals(2, history.get(0).getId(), "История сохранена неверно");
    }

}