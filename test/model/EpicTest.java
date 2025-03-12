package service;

import model.Epic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    @Test
    public void EqualityTest() {
        Epic e0 = new Epic("Эпик 1", "Тестируем эпик 1", 1);
        Epic e1 = new Epic("Эпик 2", "Тестируем эпик 2", 1);
        assertEquals(e0, e1, "Экземпляры эпиков не равны");
    }
}
