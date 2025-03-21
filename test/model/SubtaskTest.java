package test.model;

import model.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    public void equalityTest() {
        Subtask s0 = new Subtask("Подзадача 1", "Тестируем подзадачу 1", 1);
        Subtask s1 = new Subtask("Подзадача 2", "Тестируем подзадачу 2", 1);
        assertEquals(s0, s1, "Экземпляры подзадач не равны");
    }
}
