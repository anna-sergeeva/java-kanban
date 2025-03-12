package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    public void equalityTest() {
        Epic e0 = new Epic("Эпик 1", "Тестируем эпик 1", 1);
        Epic e1 = new Epic("Эпик 2", "Тестируем эпик 2", 1);

        Subtask s0 = new Subtask("Подзадача 1", "Тестируем подзадачу 1", StatusOfTask.NEW, e0);
        Subtask s1 = new Subtask("Подзадача 2", "Тестируем подзадачу 2", StatusOfTask.NEW, e1);
        assertEquals(s0, s1, "Экземпляры подзадач не равны");
    }
}
