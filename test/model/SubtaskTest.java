package test.model;

import model.Subtask;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    public void equalityByIdTest() {
        Subtask s0 = new Subtask("Подзадача", "Подзадача", 1, Duration.ofMinutes(2),
                LocalDateTime.now());
        Subtask s1 = new Subtask("Подзадача", "Подзадача", 1, Duration.ofMinutes(2),
                LocalDateTime.now());
        assertEquals(s0, s1, "Экземпляры подзадач не равны");
    }
}

