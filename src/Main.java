import model.Epic;
import model.StatusOfTask;
import model.Subtask;
import model.Task;
import service.Manager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        // Создаём 2 задачи
        Task task1 = new Task("Купить продукты", "Купить продукты для НГ вечеринки", 0, StatusOfTask.NEW);
        Task task2 = new Task("Постирать вещи", "Постирать вещи ребёнка", 0, StatusOfTask.NEW);

        int taskId1 = manager.createTask(task1);
        int taskId2 = manager.createTask(task2);

        // Создаём эпик с 2 подзадачами
        Epic epic1 = new Epic("Собраться на корпоратив", "Выполнить задачи для похода на корпоратив", 0, StatusOfTask.NEW, new ArrayList<Integer>());
        int epicId1 = manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Купить платье", "Купить вечернее платье, не дороже 10000", 0, StatusOfTask.NEW, epicId1);
        Subtask subtask2 = new Subtask("Сделать прическу", "Записаться в салон на укладку", 0, StatusOfTask.NEW, epicId1);
        int subtaskId1 = manager.createSubtask(subtask1);
        int subtaskId2 = manager.createSubtask(subtask2);

        // Создаём эпик с 1 подзадачей
        Epic epic2 = new Epic("Получить визу в Испанию", "Выполнить все пункты для визы в Испанию", 0, StatusOfTask.NEW, new ArrayList<Integer>());
        int epicId2 = manager.createEpic(epic2);

        Subtask subtask3 = new Subtask("Собрать документы для визы", "", 0, StatusOfTask.NEW, epicId2);
        int subtaskId3 = manager.createSubtask(subtask3);

        // Распечатываем списки эпиков, задач и подзадач
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        // Изменяем статусы созданных объектов и распечатаем
        Task task1upd = new Task("Купить диван", "", taskId1, StatusOfTask.DONE);
        Task task2upd = new Task("Помыть полы", "", taskId2, StatusOfTask.DONE);
        Subtask subtask1upd = new Subtask("Заказать такси", "Заказать такси до места мероприятия", subtaskId1, StatusOfTask.DONE, epicId1);
        Subtask subtask2upd = new Subtask("Купить подарки", "Купить подарки коллегам", subtaskId2, StatusOfTask.DONE, epicId1);
        Subtask subtask3upd = new Subtask("Купить билеты", "Купить билеты на самолет и на поезд", subtaskId3, StatusOfTask.DONE, epicId2);

        manager.updateTask(task1upd);
        manager.updateTask(task2upd);
        manager.updateSubtask(subtask1upd);
        manager.updateSubtask(subtask2upd);
        manager.updateSubtask(subtask3upd);

        System.out.println("\nСписок после изменения статусов задач");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        // Удаляем 1 задачу и 1 эпик и распечатываем состояние сущностей после удаления
        manager.delTaskById(taskId1);
        manager.delEpicById(epicId1);

        System.out.println("\nСписок после удаления задач");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }
}

