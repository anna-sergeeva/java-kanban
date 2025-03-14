package test.model;

import model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void equalityTest() {
        Task t0 = new Task("Задача 1", "Тестируем задачу 1");
        Task t1 = new Task("Задача2", "Тестируем задачу 2");
        assertEquals(t0, t1, "Экземпляры задач не равны");
    }
}
