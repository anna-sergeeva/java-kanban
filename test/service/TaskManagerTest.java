package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static model.StatusOfTask.NEW;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        initTasks();
    }

    protected void initTasks() {
        // Создадим 2 задачи;
        Task task1 = new Task("Задача 1", "Тестируем задачу 1", 1);
        int task1Id = taskManager.addTask(task1);

        Task task2 = new Task("Задача2", "Тестируем задачу 2", 1);
        int task2Id = taskManager.addTask(task1);

        // Создадим 1 эпик
        Epic epic3 = new Epic("Эпик 1", "Тестируем эпик 1", 1);
        int epic3Id = taskManager.addEpic(epic3);

        // Создадим 2 подзадачи
        Subtask subtask4 = new Subtask("Подзадача 1", "Тестируем подзадачу 1", StatusOfTask.NEW, epic3);
        int subtask4Id = taskManager.addSubtask(subtask4);

        Subtask subtask5 = new Subtask("Подзадача 2", "Тестируем подзадачу 2", StatusOfTask.NEW, epic3);
        int subtask5Id = taskManager.addSubtask(subtask4);
    }

    @Test
    void addTaskTest() {
        Task getTask1 = taskManager.getTaskById(1);
        assertNotNull(getTask1, "Идентификатор задачи не найден");
        assertEquals(2, getTask1.getId(), "Идентификаторы задач не совпадают");
        assertNotNull(taskManager.getAllTasks(), "Задачи в списке не найдены");
        assertEquals(2, taskManager.getAllTasks().size(), "Число созданнных и найденных в списке задач не совпадает");
    }

    @Test
    void addEpicTest() {
        Epic getEpic1 = taskManager.getEpicById(3);
        assertNotNull(getEpic1, "Идентификатор эпика не найден");
        assertEquals(3, getEpic1.getId(), "Идентификаторы эпиков не совпадают");
        assertNotNull(taskManager.getAllEpics(), "Эпики в списке не найдены");
        assertEquals(1, taskManager.getAllEpics().size(), "Число созданных и найденных в списке эпиков не совпадает");
        assertNotNull(getEpic1.getSubtaskId(), "Подзадачи в эпике не созданы");
        assertEquals(NEW, getEpic1.getStatus(), "Статус созданного эпика должен быть NEW");

    }

    @Test
    void addSubtaskTest() {
        Epic getEpicOfSubtask = taskManager.getEpicById(3);
        Subtask getSubtask1 = taskManager.getSubtaskById(4);
        assertNotNull(getSubtask1, "Идентификатор подзадачи не найден");
        assertEquals(5, getSubtask1.getId(), "Идентификаторы подзадач не равны друг другу");
        assertNotNull(taskManager.getAllSubtasks(), "Подзадачи в списке не найдены");
        assertEquals(2, taskManager.getAllSubtasks().size(), "Число созданных и найденных в списке подзадач не совпадает");
        assertNotNull(getEpicOfSubtask, "Эпик подзадачи не найден");
        assertNotNull(taskManager.getSubtasksOfEpic(getEpicOfSubtask), "Список подзадач в эпике не обновился");
        assertEquals(NEW, getEpicOfSubtask.getStatus(), "Статус  эпика не NEW");
    }

    @Test
    void getSubtasksOfEpicTest() {
        //Epic epic3 = new Epic("E1", "", 1, new ArrayList<Integer>());
        Epic epic3 = new Epic("Эпик 1", "Тестируем эпик 1", 1);
        Subtask subtask4 = new Subtask("Подзадача 1", "Тестируем подзадачу 1", StatusOfTask.NEW, epic3);

        //Subtask subtask4 = new Subtask("E1S1", "", 1, 3);
        int subtask4Id = taskManager.addSubtask(subtask4);
        assertNotNull(taskManager.getSubtasksOfEpic(epic3), "Эпик не содержит подзадач");
        assertEquals(0, taskManager.getSubtasksOfEpic(epic3).size(), "Число подзадач в эпике не совпадает с ожидаемым");
    }

    @Test
    void getHistoryTest() {
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getTaskById(1);
        List<Task> history = taskManager.getHistory();
        assertEquals(3, history.size(), "История записей сформирована неверно");
    }

    @Test
    void updateTaskTest() {
        Task task1 = new Task("T1", "", 1);
        int task1Id = taskManager.addTask(task1);
        taskManager.updateTask(task1);
        Task updTask1 = taskManager.getTaskById(task1Id);
        assertEquals(task1, updTask1, "Одинаковые задачи не совпадают");
        taskManager.updateTask(new Task("T1", "upT1", task1Id));
    }

    @Test
    void updateEpicTest() {
        Epic epic3 = new Epic("Эпик 1", "Тестируем эпик 1", 1);
        //Epic epic3 = new Epic("E1", "", 1, new ArrayList<Integer>());
        int epic3Id = taskManager.addEpic(epic3);
        taskManager.updateEpic(epic3);
        Epic updEpic3 = taskManager.getEpicById(epic3Id);
        assertEquals(epic3, updEpic3, "Одинаковые задачи не совпадают.");
        //taskManager.updateEpic(new Epic("Epic 3", "updE3", epic3Id, epic3.getSubtaskId()));
    }

    @Test
    void updateSubtaskTest() {
        Epic epic3 = new Epic("Эпик 1", "Тестируем эпик 1", 1);
        Subtask subtask4 = new Subtask("Подзадача 1", "Тестируем подзадачу 1", StatusOfTask.NEW, epic3);

        //Subtask subtask4 = new Subtask("E1S1", "", 1, 3);
        int subtask4Id = taskManager.addSubtask(subtask4);
        taskManager.updateSubtask(subtask4);
        Subtask updSubtask4 = taskManager.getSubtaskById(subtask4Id);
        assertEquals(subtask4, updSubtask4, "Одинаковые задачи не совпадают.");
        //taskManager.updateSubtask(new Subtask("S1", "upS1", subtask4Id, 3));
    }

    @Test
    void removeTaskByIdTest() {
        taskManager.removeTaskById(1);
        assertNull(taskManager.getSubtaskById(1), "Задача не удалена");
        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Удалились все задачи.");
        assertEquals(1, tasks.size(), "Неверное количество оставшихся задач.");
    }

    @Test
    void removeEpicByIdTest() {
        taskManager.removeEpicById(3);
        assertNull(taskManager.getSubtaskById(3), "Задача не удалилась");
        List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Удалились все задачи.");
        assertEquals(0, epics.size(), "Неверное количество оставшихся задач.");
    }

    @Test
    void removeSubtaskByIdTest() {
        taskManager.removeSubtaskById(4);
        assertNull(taskManager.getSubtaskById(4), "Задача не удалена");
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertNotNull(subtasks, "Удалились все задачи.");
        assertEquals(1, subtasks.size(), "Неверное количество оставшихся задач.");
    }


    @Test
    void removeAllTasksTest() {
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getAllTasks().size(), "Удалены не все задачи.");
    }

    @Test
    void removeAllEpicsTest() {
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getAllEpics().size(), "Удалены не все эпики.");
    }

    @Test
    void removeAllSubtasksTest() {
        taskManager.removeAllSubtasks();
        assertEquals(0, taskManager.getAllSubtasks().size(), "Удалены не все подзадачи.");
    }

}