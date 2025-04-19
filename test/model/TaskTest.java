package test.model;

import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void equalityByIdTest() {
        Task t0 = new Task("Задача", "Задача",Duration.ofMinutes(5), LocalDateTime.now());
        Task t1 = new Task("Задача", "Задача",Duration.ofMinutes(5), LocalDateTime.now());
        assertEquals(t0, t1, "Экземпляры задач не равны");
    }
}