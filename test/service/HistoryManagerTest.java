package test.service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class HistoryManagerTest {

    HistoryManager historyManager;
    Task task;
    Epic epic;
    Subtask subtask1, subtask2;


    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Задача", "");
        epic = new Epic("Эпик", "");
        subtask1 = new Subtask("Подзадача", "Подзадача", epic.getId(), Duration.ofMinutes(2),
                LocalDateTime.now());
        subtask2 = new Subtask("Подзадача", "Подзадача", epic.getId(), Duration.ofMinutes(2),
                LocalDateTime.now());
        task.setId(1);
        epic.setId(2);
        subtask1.setId(3);
        subtask2.setId(4);
    }

    @Test
    void addTaskToHistoryTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(List.of(task, epic, subtask1, subtask2), history, "История пустая");
        assertEquals(1, task.getId(), "История сохранена неверно");
        assertEquals(2, epic.getId(), "История сохранена неверно");
        assertEquals(4, subtask2.getId(), "История сохранена неверно");

    }

    @Test
    void maxAttemptsToHistoryTest() {
        int i = 0;
        while (i < 20) {
            historyManager.add(task);;
            i++;
        }
        List<Task> history = historyManager.getHistory();
        assertEquals(List.of(task), history, "Число записей в истории должно быть не более 1");
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
        assertEquals(3, history.get(1).getId(), "История сохранена некорректно");
        //удаление из середины истории
        historyManager.add(subtask2);
        historyManager.remove(3);
        history = historyManager.getHistory();
        assertEquals(List.of(epic, subtask2), history, "История сохранена некорректно");
        assertEquals(2, history.get(0).getId(), "История сохранена некорректно");
        assertEquals(4, history.get(1).getId(), "История сохранена некорректно");
        //удаление с конца истории
        historyManager.remove(4);
        history = historyManager.getHistory();
        assertEquals(List.of(epic), history, "История сохранена некорректно");
        assertEquals(2, history.get(0).getId(), "История сохранена некорректно");
    }

    @Test
    public void removeFirstElementTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.remove(task.getId());
        assertEquals(List.of(epic, subtask1), historyManager.getHistory(),
                "Удален не первый элемент в истории просмотров");
    }

    @Test
    public void removeMiddleElementTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.remove(epic.getId());
        assertEquals(List.of(task, subtask1), historyManager.getHistory(),
                "Удален не второй из трех элементов в истории просмотров");
    }

    @Test
    public void removeLastElementTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.remove(subtask1.getId());
        assertEquals(List.of(task, epic), historyManager.getHistory(),
                "Удален не последний элемент в истории просмотров");
    }

}