package test.service;

import org.junit.jupiter.api.BeforeEach;
import service.InMemoryTaskManager;
import model.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static model.StatusOfTask.NEW;
import static org.junit.jupiter.api.Assertions.*;


public class TaskManagerTest{
    InMemoryTaskManager taskManager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void initTasks() {
        taskManager = new InMemoryTaskManager();
        task = new Task("Задача", "Задача");
        taskManager.addNewTask(task);
        epic = new Epic("Эпик", "Эпик");
        taskManager.addNewEpic(epic);
        subtask = new Subtask("Подзадача", "Подзадача", epic.getId());
        taskManager.addNewSubtask(subtask);
    }

    @Test
    void addTaskTest() {
        assertEquals(1, taskManager.getListOfTasks().size(), "Число задач определяется некорректно");
        assertNotEquals(task.getId(), subtask.getId(), "Идентификатор задач работает некорректно");
    }

    @Test
    void addEpicTest() {
        assertEquals(1, taskManager.getListOfEpics().size(), "Число эпиков определяется некорректно");
        assertNotEquals(task.getId(), epic.getId(), "Идентификатор задач работает некорректно");

        assertNotNull(epic.getId(), "Идентификатор эпика не найден");assertNotNull(taskManager.getListOfEpics(), "Эпики в списке не найдены");
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
        assertNotNull(taskManager.getListOfSubtasksOfEpic(epic), "Список подзадач в эпике не обновился");
    }

    @Test
    void getSubtasksOfEpicTest() {
        assertNotNull(taskManager.getListOfSubtasksOfEpic(epic), "Эпик не содержит подзадач");
        assertEquals(1, taskManager.getListOfSubtasksOfEpic(epic).size(), "Число подзадач в эпике не совпадает с ожидаемым");
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
    void updateTaskTest() {
        taskManager.updateTask(task);
        Task updTask1 = taskManager.getTaskById(1);
        assertEquals(task, updTask1, "Одинаковые задачи не совпадают");
    }

    @Test
    void updateEpicTest() {
        taskManager.updateEpic(epic);
        Epic updEpic3 = taskManager.getEpicById(2);
        assertEquals(epic, updEpic3, "Одинаковые задачи не совпадают.");
    }

    @Test
    void updateSubtaskTest() {
        Subtask updSubtask4 = taskManager.getSubtaskById(3);
        assertEquals(subtask, updSubtask4, "Одинаковые задачи не совпадают.");
    }

    @Test
    void removeTaskByIdTest() {
        taskManager.removeTaskById(1);
        assertNull(taskManager.getTaskById(1), "Задача не удалена");
        List<Task> tasks = taskManager.getListOfTasks();
        assertNotNull(tasks, "Удалились все задачи.");
        assertEquals(0, tasks.size(), "Неверное количество оставшихся задач.");
    }

    @Test
    void removeSubtaskByIdTest() {
        taskManager.removeSubtaskById(3);
        assertNull(taskManager.getSubtaskById(3), "Задача не удалена");
        List<Subtask> subtasks = taskManager.getListOfSubtasks();
        assertNotNull(subtasks, "Удалились все задачи.");
        assertEquals(0, subtasks.size(), "Неверное количество оставшихся задач.");
    }

    @Test
    void removeEpicByIdTest() {
        taskManager.removeEpicById(2);
        assertNull(taskManager.getEpicById(2), "Задача не удалилась");
        List<Epic> epics = taskManager.getListOfEpics();
        assertNotNull(epics, "Удалились все задачи.");
        assertEquals(0, epics.size(), "Неверное количество оставшихся задач.");
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

