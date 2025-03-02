package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void equalityByIdTest() {
        Task t0 = new Task("Задача 1", "Тестируем задачу 1", 1);
        Task t1 = new Task("Задача2", "Тестируем задачу 2", 1);
        assertEquals(t0, t1, "Экземпляры задач не равны");
    }

}
