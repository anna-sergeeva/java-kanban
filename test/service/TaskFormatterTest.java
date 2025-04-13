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
        task = new Task(1, "Task", "Test", Duration.ofMinutes(5), LocalDateTime.of(5, 5, 5, 5, 5));
        epic = new Epic(2, "Epic", "Test");
        subtask = new Subtask(3, "Subtask", "Test", 2, Duration.ofMinutes(10), LocalDateTime.of(2, 2, 2, 2, 2));
    }

    @Test
    public void toStringTest() {
        assertEquals("1,TASK,Task,NEW,Test,PT5M,0005-05-05T05:05", TaskFormatter.toString(task),
                "toString() некорректно переводит Task в String");
        assertEquals("2,EPIC,Epic,NEW,Test,PT0S,null", TaskFormatter.toString(epic),
                "toString() некорректно переводит Epic в String");
        assertEquals("3,SUBTASK,Subtask,NEW,Test,PT10M,0002-02-02T02:02,2", TaskFormatter.toString(subtask),
                "toString() некорректно переводит Subtask в String");
    }

    @Test
    public void fromStringTest() {
        assertEquals(task, TaskFormatter.fromString("1,TASK,Task,NEW,Test,PT10M,0001-01-01T01:01"),
                "taskFromString() некорректно переводит String в Task");
        assertEquals(epic, TaskFormatter.fromString("2,EPIC,Epic,NEW,Test,PT0M,0001-01-01T01:01"),
                "taskFromString() некорректно переводит String в Epic");
        assertEquals(subtask, TaskFormatter.fromString("3,SUBTASK,Subtask,NEW,Test,PT10M,0002-02-02T02:02,2"),
                "taskFromString() некорректно переводит String в Subtask");
    }
}
