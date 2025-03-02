import model.Epic;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        // Создаем 2 задачи
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", 2);
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);

        // Создаем 1 эпик c 3 подзадачами и эпик без подзадач
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", 3, new ArrayList<Integer>());
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", 4, new ArrayList<Integer>());

        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 5, 3);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 6, 3);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", 7, 3);

        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        int subtask3Id = taskManager.addSubtask(subtask3);

        // Просмотрим задачи и вывели историю после каждого запроса
        taskManager.getTaskById(task1Id);
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(task1Id);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subtask2Id);
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(epic2Id);
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(task2Id);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subtask1Id);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subtask3Id);
        System.out.println(taskManager.getHistory());

        //Удалим задачу из истории и проверили, что при печати она не будет выводиться
        taskManager.removeTaskById(task1Id);
        System.out.println(taskManager.getHistory());

        //Удалим эпик с 3 подзадачами и убедились, что в истории нет ни эпика, ни его подзадач
        taskManager.removeEpicById(epic1Id);
        System.out.println(taskManager.getHistory());
    }
}