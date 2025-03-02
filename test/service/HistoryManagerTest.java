package service;

import model.Task;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class HistoryManagerTest {
        HistoryManager historyManager = new InMemoryHistoryManager();

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
    void maxAttemptsToHistory() {
        Task task1 = new Task("T1", "", 1);
        int i = 0;
        while (i < 20) {
            historyManager.add(task1);;
            i++;
        }
        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size(), "Число записей в истории должно быть не более 10");
    }
}
