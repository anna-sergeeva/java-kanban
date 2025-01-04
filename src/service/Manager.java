package service;

import model.Epic;
import model.StatusOfTask;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Manager {
    Integer id = 0;

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int idCounter() {
        id++;
        return id;
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtaskId().equals(Collections.emptyList())) {
            epic.setStatus(StatusOfTask.NEW);
            return;
        }
        boolean isStatusNew = false;
        boolean isStatusInProgress = false;
        boolean isStatusDone = false;
        for (Integer subtaskId : epic.getSubtaskId()) {
            if (subtasks.get(subtaskId).getStatus() == StatusOfTask.NEW) {
                isStatusNew = true;
            }
            if (subtasks.get(subtaskId).getStatus() == StatusOfTask.IN_PROGRESS) {
                isStatusInProgress = true;
            }
            if (subtasks.get(subtaskId).getStatus() == StatusOfTask.DONE) {
                isStatusDone = true;
            }
        }
        if (isStatusNew && !isStatusInProgress && !isStatusDone) {
            epic.setStatus(StatusOfTask.NEW);
        } else if (isStatusDone && !isStatusNew && !isStatusInProgress) {
            epic.setStatus(StatusOfTask.DONE);
        } else {
            epic.setStatus(StatusOfTask.IN_PROGRESS);
        }
    }

    public Integer createTask(Task task) {
        final int id = idCounter();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public Integer createEpic(Epic epic) {
        final int id = idCounter();
        epic.setId(id);
        updateEpicStatus(epic);
        epics.put(id, epic);
        return id;
    }

    public Integer createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        final int id = idCounter();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.getSubtaskId().add(id);
        updateEpicStatus(epic);
        return id;
    }

    public void updateTask(Task task) {
        final int id = task.getId();
        final Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.put(id, task);
    }

    public void updateEpic(Epic epic) {
        final int id = epic.getId();
        final Task savedEpic = epics.get(id);
        if (savedEpic == null) {
            return;
        }
        epics.put(id, epic);
    }

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

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(this.epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }

    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskId()) {
            subtasksOfEpic.add(subtasks.get(subtaskId));
        }
        return subtasksOfEpic;
    }

    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(Integer id) {
        return subtasks.get(id);
    }

    public void delAllTasks() { tasks.clear(); }

    public void delAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void delAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().clear();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    public void delTaskById(Integer id) {
        tasks.remove(id);
    }

    public void delEpicById(Integer id) {
        for (Integer subtaskId : epics.get(id).getSubtaskId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public void delSubtasksById(Integer id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.getSubtaskId().remove(id);
        updateEpicStatus(epic);
        subtasks.remove(id);
    }
}