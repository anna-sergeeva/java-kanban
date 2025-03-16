package service;

import model.Task;
import model.Epic;
import model.Subtask;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    int addNewSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    ArrayList<Task> getListOfTasks();

    ArrayList<Epic> getListOfEpics();

    ArrayList<Subtask> getListOfSubtasks();

    ArrayList<Subtask> getListOfSubtasksOfEpic(Epic epic);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    void removeTaskById(Integer id);

    void removeEpicById(Integer epicId);

    void removeSubtaskById(Integer id);
}
