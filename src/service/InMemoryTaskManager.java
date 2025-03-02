package service;

import model.Epic;
import model.StatusOfTask;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

public class InMemoryTaskManager implements TaskManager {
    protected Integer id = 1;

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Integer addTask(Task task) {
        final int id = idCounter();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public Integer addEpic(Epic epic) {
        final int id = idCounter();
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return -1;
        }
        final int id = idCounter();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskId(id);;
        updateEpicStatus(epic);
        return id;
    }


    @Override
    public void updateTask(Task task) {
        final int id = task.getId();
        final Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.put(id, task);
    }

    @Override
    public void updateEpic(Epic epic) {
        final int id = epic.getId();
        final Task savedEpic = epics.get(id);
        if (savedEpic == null) {
            return;
        }
        epics.put(id, epic);
    }


    @Override
    public void updateSubtask(Subtask subtask) {
        final int id = subtask.getId();
        final Task savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }


    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskId()) {
            subtasksOfEpic.add(subtasks.get(subtaskId));
        }
        return subtasksOfEpic;
    }


    @Override
    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }


    @Override
    public void delAllTasks() {
        tasks.clear();
    }

    @Override
    public void delAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void delAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtaskId();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public void delTaskById(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void delEpicById(Integer epicId) {
        Epic epic = epics.remove(epicId);
        if (epic == null) {
            return;
        }
        for (Subtask subtask : getSubtasks()) {
            if (subtask.getEpicId() == epicId) {
                subtasks.remove(subtask.getId());
            }
        }
    }


    @Override
    public void delSubtaskById(Integer id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        if (epic == null) {
            return;
        }
        epic.getSubtaskId().remove(id);
        updateEpicStatus(epic);
        subtasks.remove(id);
    }


    private int idCounter() {
        return id++;
    }


    private void updateEpicStatus(Epic epic) {
        int newCount = 0;
        int doneCount = 0;
        for (int itemId : epic.getSubtaskId()) {
            Subtask subtask = subtasks.get(itemId);
            if (subtask.getStatus() == StatusOfTask.NEW) {
                newCount++;
            }
            if (subtask.getStatus() == StatusOfTask.DONE) {
                doneCount++;
            }
        }
        if (newCount == epic.getSubtaskId().size()) {
            epic.setStatus(StatusOfTask.NEW);
        } else if (doneCount == epic.getSubtaskId().size()) {
            epic.setStatus(StatusOfTask.DONE);
        } else {
            epic.setStatus(StatusOfTask.IN_PROGRESS);
        }
    }

}
