import model.Epic;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        // Создадим 2 задачи
        int task1Id = taskManager.addTask(new Task("Задача 1", "Описание задачи 1", 1));
        int task2Id = taskManager.addTask(new Task("Задача 2", "Описание задачи 2", 1));

        // Создадим 1 эпик
        int epic1Id = taskManager.addEpic(new Epic("Эпик 1", "Описание эпика 1", 1, new ArrayList<Integer>()));

        // Просмотрим задачи
        taskManager.getTaskById(task1Id);
        taskManager.getTaskById(task2Id);


        // Просмотрим историю показов задач
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        // Просмотрим эпик 5 раз
        int i = 0;
        while (i < 5) {
            taskManager.getEpicById(epic1Id);
            i++;
        }

        // Просмотрим историю показов задач
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}