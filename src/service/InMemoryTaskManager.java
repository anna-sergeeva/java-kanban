package service;

import model.Epic;
import model.StatusOfTask;
import model.Subtask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected Integer id = 1;

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }


    @Override
    public int addTask(Task task) {
        final int id = idCounter();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpic().getId());
        if (epic == null) {
            return -1;
        }
        final int id = idCounter();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskToEpic(subtask);;
        updateEpicStatus(epic);
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        final int id = idCounter();
        epic.setId(id);
        epics.put(id, epic);
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
        Epic epic = epics.get(subtask.getEpic().getId());
        updateEpicStatus(epic);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }


    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        return epic.getSubtasks();
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
    public void removeAllTasks() {
        Set<Integer> taskKeys = tasks.keySet();
        for (Integer key :  taskKeys) {
            historyManager.remove(key);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        Set<Integer> subtaskKeys = subtasks.keySet();
        Set<Integer> epicKeys = epics.keySet();
        for (Integer subtaskKey : subtaskKeys) {
            historyManager.remove(subtaskKey);
        }
        for (Integer epicKey : epicKeys) {
            historyManager.remove(epicKey);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        Set<Integer> subtaskKeys = subtasks.keySet();
        for (Integer subtaskKey : subtaskKeys) {
            historyManager.remove(subtaskKey);
        }
        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }


    @Override
    public void removeEpicById(int epicId) {
        Epic epic = epics.remove(epicId);
        historyManager.remove(epicId);
        if (epic == null) {
            return;
        }
        for (Subtask subtask : getAllSubtasks()) {
            if (subtask.getEpic().getId() == epicId) {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
            }
        }
    }


    @Override
    public void removeSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id).getEpic().getId());
        Subtask subtask = getSubtaskById(id);
        if (epic == null) {
            return;
        }
        epic.removeSubtask(subtask);
        updateEpicStatus(epic);
        subtasks.remove(id);
        historyManager.remove(id);
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