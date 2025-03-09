package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    public void EqualityByIdTest() {
        Epic e0 = new Epic("Эпик 1", "Тестируем эпик 1", 1);
        Epic e1 = new Epic("Эпик 2", "Тестируем эпик 2", 1);
        assertEquals(e0, e1, "Экземпляры эпиков не равны");
    }
}
