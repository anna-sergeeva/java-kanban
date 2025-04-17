package test.service;

import org.junit.jupiter.api.BeforeEach;
import model.*;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static model.StatusOfTask.NEW;
import static org.junit.jupiter.api.Assertions.*;


public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;


    @Test
    void addTaskTest() {
        assertEquals(1, taskManager.getListOfTasks().size(), "Число задач определяется некорректно");
        assertNotEquals(task.getId(), subtask.getId(), "Идентификатор задач работает некорректно");
        assertEquals("Задача", task.getName(), "Имя задачи изменилось.");
        assertEquals("Задача", task.getDescription(), "Описание задачи изменилось.");
        assertEquals(StatusOfTask.NEW, task.getStatus(), "Задача не находится в состоянии NEW");
    }

    @Test
    void addEpicTest() {
        assertEquals(1, taskManager.getListOfEpics().size(), "Число эпиков определяется некорректно");
        assertNotEquals(task.getId(), epic.getId(), "Идентификатор задач работает некорректно");

        assertNotNull(epic.getId(), "Идентификатор эпика не найден");
        assertNotNull(taskManager.getListOfEpics(), "Эпики в списке не найдены");
        assertEquals(1, taskManager.getListOfEpics().size(), "Число созданных и найденных в списке эпиков не совпадает");
        assertNotNull(epic.getSubtaskId(), "Подзадачи в эпике не созданы");
        assertEquals(NEW, epic.getStatus(), "Статус созданного эпика должен быть NEW");
        assertEquals(NEW, epic.getStatus(), "Статус  эпика не NEW");

    }

    @Test
    void addSubtaskTest() {
        assertEquals(1, taskManager.getListOfSubtasks().size(), "Число подзадач определяется некорректно");
        assertNotEquals(epic.getId(), subtask.getId(), "Идентификатор задач работает некорректно");
        assertNotNull(subtask, "Идентификатор подзадачи не найден");
        assertEquals(3, subtask.getId(), "Идентификаторы подзадач не равны друг другу");
        assertNotNull(taskManager.getListOfSubtasks(), "Подзадачи в списке не найдены");
        assertEquals(1, taskManager.getListOfSubtasks().size(), "Число созданных и найденных в списке подзадач не совпадает");
        assertNotNull(subtask.getEpicId(), "Эпик подзадачи не найден");
        assertNotNull(taskManager.getListOfSubtasksOfEpic(epic.getId()), "Список подзадач в эпике не обновился");
    }

    @Test
    void getSubtasksOfEpicTest() {
        assertNotNull(taskManager.getListOfSubtasksOfEpic(epic.getId()), "Эпик не содержит подзадач");
        assertEquals(1, taskManager.getListOfSubtasksOfEpic(epic.getId()).size(), "Число подзадач в эпике не совпадает с ожидаемым");
    }

    @Test
    void getHistoryTest() {
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
        taskManager.getTaskById(1);
        List<Task> history = taskManager.getHistory();
        assertEquals(3, history.size(), "История записей сформирована неверно");
    }


    @Test
    public void updateEpicStatusTest() {
        subtask.setStatus(StatusOfTask.IN_PROGRESS);
        taskManager.updateEpic(epic);
        assertEquals(epic.getStatus(), StatusOfTask.IN_PROGRESS, "функция updateEpicStatus неправильно обновила статус эпика");
        subtask.setStatus(StatusOfTask.DONE);
        taskManager.updateEpic(epic);
        assertEquals(epic.getStatus(), StatusOfTask.DONE, "функция updateEpicStatus неправильно обновила статус эпика");
        assertEquals(subtask.getStatus(), StatusOfTask.DONE, "функция updateEpicStatus неправильно обновила статус эпика");
        Subtask subtask2 = new Subtask("Подазадача2", "Подзадача2", epic.getId(), Duration.ofMinutes(10),
                LocalDateTime.now().minusMinutes(20L));
        taskManager.addNewSubtask(subtask2);
        assertEquals(epic.getStatus(), StatusOfTask.IN_PROGRESS,
                "функция updateEpicStatus после добавления новой подзадачи некорректно обновила статус эпика");
    }

    @Test
    void updateSubtaskTest() {
        Subtask updSubtask4 = taskManager.getSubtaskById(3);
        assertEquals(subtask, updSubtask4, "Одинаковые задачи не совпадают.");
    }

    @Test
    public void updateTest() {
        Task task2 = new Task(task.getId(), "Задача2", "Задача2", Duration.ofMinutes(10),
                LocalDateTime.now().minusMinutes(80L));
        Epic epic2 = new Epic(epic.getId(), "Эпик2", "Эпик2");
        Subtask subtask2 = new Subtask(subtask.getId(), "Подзадача2", "Подзадача2", epic.getId(),
                Duration.ofMinutes(10), LocalDateTime.now().minusMinutes(100L));
        taskManager.updateTask(task2);
        taskManager.updateEpic(epic2);
        taskManager.updateSubtask(subtask2);
        assertEquals(task.getName(), task2.getName(), "задача не обновлена");
        assertEquals(epic.getName(), epic2.getName(), "эпик не обновлен");
        assertEquals(subtask.getName(), subtask2.getName(), "подзадача не обновлена");
    }


    @Test
    void removeTaskByIdTest() {
        taskManager.removeTaskById(1);
        assertNull(taskManager.getTaskById(1), "Задача не удалена");
        List<Task> tasks = taskManager.getListOfTasks();
        assertNotNull(tasks, "Удалены не все задачи");
        assertEquals(0, tasks.size(), "Некорректное число задач после удаления");
    }

    @Test
    void removeEpicByIdTest() {
        taskManager.removeEpicById(2);
        assertNull(taskManager.getEpicById(2), "Эпик не удален");
        List<Epic> epics = taskManager.getListOfEpics();
        assertNotNull(epics, "Удалены не все эпики");
        assertEquals(0, epics.size(), "Некорректное число эпиков после удаления");
    }

    @Test
    void removeSubtaskByIdTest() {
        taskManager.removeSubtaskById(subtask.getId());
        assertNull(taskManager.getSubtaskById(2), "Задача не удалена");
        List<Subtask> subtasks = taskManager.getListOfSubtasks();
        assertNotNull(subtasks, "Удалены не все подзадачи.");
        assertEquals(0, subtasks.size(), "Некорректное число подзадач после удаления");
    }

    @Test
    void removeAllTasksTest() {
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getListOfTasks().size(), "Удалены не все задачи.");
    }

    @Test
    void removeAllEpicsTest() {
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getListOfEpics().size(), "Удалены не все эпики.");
    }

    @Test
    void removeAllSubtasksTest() {
        taskManager.removeAllSubtasks();
        assertEquals(0, taskManager.getListOfSubtasks().size(), "Удалены не все подзадачи.");
    }

}

