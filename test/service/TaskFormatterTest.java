package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import service.TaskFormatter;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskFormatterTest {
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void setUp() {
        task = new Task(1, "Задача", "Тестовая задача", Duration.ofMinutes(5), LocalDateTime.of(5, 5, 5, 5, 5));
        epic = new Epic(2, "Эпик", "Тестовый эпик");
        subtask = new Subtask(3, "Подзадача", "Тестовая подзадача", 2, Duration.ofMinutes(10), LocalDateTime.of(2, 2, 2, 2, 2));
    }

    @Test
    public void toStringTest() {
        assertEquals("1,TASK,Задача,NEW,Тестовая задача,PT5M,0005-05-05T05:05", TaskFormatter.toString(task),
                "функция toString() некорректно переводит Task (задачу) в String (строку)");
        assertEquals("2,EPIC,Эпик,NEW,Тестовый эпик,PT0S,null", TaskFormatter.toString(epic),
                "функция toString() некорректно переводит Epic (эпик) в String (строку)");
        assertEquals("3,SUBTASK,Подзадача,NEW,Тестовая подзадача,PT10M,0002-02-02T02:02,2", TaskFormatter.toString(subtask),
                "функция toString() некорректно переводит Subtask (подзадачу) в String (строку)");
    }

    @Test
    public void fromStringTest() {
        assertEquals(task, TaskFormatter.fromString("1,TASK,Task,NEW,Test,PT10M,0001-01-01T01:01"),
                "функция fromString() некорректно переводит String (строку) в Task (задачу)");
        assertEquals(epic, TaskFormatter.fromString("2,EPIC,Epic,NEW,Test,PT0M,0001-01-01T01:01"),
                "функция fromString() некорректно переводит String (строку) в Epic (эпик)");
        assertEquals(subtask, TaskFormatter.fromString("3,SUBTASK,Subtask,NEW,Test,PT10M,0002-02-02T02:02,2"),
                "функция fromString() некорректно переводит String (строку) в Subtask (подзадачу)");
    }
}
