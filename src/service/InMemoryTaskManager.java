package service;

import model.Epic;
import model.StatusOfTask;
import model.Subtask;
import model.Task;
import java.util.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 1;

    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public int addNewTask(Task task) {
        final int id = idCounter();
        task.setId(id);
        tasks.put(task.getId(), task);
        if (task.getStartTime() == null || !isTaskCrossed(task)) {
            return -1;
        }
        prioritizedTasks.add(task);
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = idCounter();
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return -1;
        }
        final int id = idCounter();
        subtask.setId(id);
        epic.getSubtaskId().add(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        if (subtask.getStartTime() == null || !isTaskCrossed(subtask)) {
            return -1;
        }
        prioritizedTasks.add(subtask);
        updateEpicDuration(epic);
        return id;
    }


    @Override
    public void updateTask(Task task) {
        final int id = task.getId();
        if (task.getStartTime() == null || !isTaskCrossed(task)) {
            return;
        }
        if (tasks.containsKey(id)) {
            Task newTask = tasks.get(id);
            newTask.setName(task.getName());
            newTask.setDescription(task.getDescription());
            newTask.setStatus(task.getStatus());
            newTask.setDuration(task.getDuration());
            newTask.setStartTime(task.getStartTime());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        final int id = epic.getId();
        if (epics.containsKey(id)) {
            Task newEpic = epics.get(id);
            newEpic.setName(epic.getName());
            newEpic.setDescription(epic.getDescription());
            updateEpicStatus(epic);
            updateEpicDuration(epic);
        }
    }


    @Override
    public void updateSubtask(Subtask subtask) {
        final int id = subtask.getId();
        if (subtask.getStartTime() == null || !isTaskCrossed(subtask)) {
            return;
        }
        if (subtasks.containsKey(id)) {
            Subtask newSubtask = subtasks.get(id);
            newSubtask.setName(subtask.getName());
            newSubtask.setDescription(subtask.getDescription());
            newSubtask.setStatus(subtask.getStatus());
            newSubtask.setDuration(subtask.getDuration());
            newSubtask.setStartTime(subtask.getStartTime());
            updateEpicStatus(epics.get(subtask.getEpicId()));
            updateEpicDuration(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public ArrayList<Task> getListOfTasks() {
        return new ArrayList<>(tasks.values());
    }


    @Override
    public ArrayList<Epic> getListOfEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getListOfSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Subtask> getListOfSubtasksOfEpic(int id) {
        ArrayList<Subtask> allSubtaskForEpicIdList = new ArrayList<>();
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getSubtaskId()) {
                allSubtaskForEpicIdList.add(subtasks.get(subtaskId));
            }
        }
        return allSubtaskForEpicIdList;
    }


    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void removeAllTasks() {
        Set<Integer> taskKeys = tasks.keySet();
        for (Integer key : taskKeys) {
            historyManager.remove(key);
        }
        for (Task task : tasks.values()) {
            prioritizedTasks.remove(task);
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
        for (Subtask subtask : subtasks.values()) {
            prioritizedTasks.remove(subtask);
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
        for (Subtask subtask : subtasks.values()) {
            prioritizedTasks.remove(subtask);
        }
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().clear();
            epic.setStatus(StatusOfTask.NEW);
            updateEpicDuration(epic);
        }
        subtasks.clear();
    }

    @Override
    public void removeTaskById(int id) {
        Task task = getTaskById(id);
        prioritizedTasks.remove(task);
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getSubtaskId()) {
                prioritizedTasks.remove(getSubtaskById(subtaskId));
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }


    @Override
    public void removeSubtaskById(int idSub) {
        prioritizedTasks.remove(getSubtaskById(idSub));
        if (subtasks.containsKey(idSub)) {
            Subtask sub = getSubtaskById(idSub);
            Epic epic = getEpicById(sub.getEpicId());
            epic.getSubtaskId().remove(Integer.valueOf(idSub));
            subtasks.remove(idSub);
            updateEpicStatus(epic);
            updateEpicDuration(epic);
            historyManager.remove(idSub);
        }
    }


    int idCounter() {
        return id++;
    }


    private void updateEpicStatus (Epic epic) {
        int newCount = 0;
        int doneCount = 0;
        if (!epic.getSubtaskId().isEmpty()) {
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
            return;
        }
        epic.setStatus(StatusOfTask.NEW);
    }

    private void updateEpicDuration (Epic epic){
        LocalDateTime startEpic = LocalDateTime.MAX;
        LocalDateTime endEpic = LocalDateTime.MIN;
        Duration overallDuration = Duration.ofMinutes(0);
        if (!epic.getSubtaskId().isEmpty()) {
            for (Integer id : epic.getSubtaskId()) {
                Subtask subtask = getSubtaskById(id);
                overallDuration = overallDuration.plus(subtask.getDuration());
                if (subtask.getStartTime().isBefore(startEpic)) {
                    startEpic = subtask.getStartTime();
                }
                if (subtask.getEndTime().isAfter(endEpic)) {
                    endEpic = subtask.getEndTime();
                }
            }
            epic.setStartTime(startEpic);
            epic.setDuration(overallDuration);
            epic.setEndTime(endEpic);
        } else {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
        }
    }

    private boolean isTaskCrossed (Task task){
        if (getPrioritizedTasks().isEmpty()) {
            return true;
        }
        return getPrioritizedTasks().stream()
                .filter(newTask -> task.getStartTime().isBefore(newTask.getEndTime()) &&
                        task.getEndTime().isAfter(newTask.getStartTime()))
                .findAny()
                .isEmpty();
    }
}