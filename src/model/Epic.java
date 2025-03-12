package model;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Subtask> subtasks;
    private ArrayList<Integer> subtaskId;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
    }

    public Epic(String name, String description, int id) {
        super(name, description, id);
        subtasks = new ArrayList<>();
    }

    @Override
    public void setStatus(StatusOfTask status) {

    }

    @Override
    public TypeOfTask getType() {
        return TypeOfTask.EPIC;
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void addSubtaskToEpic(Subtask subtask) {
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public void removeSubtasks() {
        subtasks.clear();
    }

    public void updateStatus() {
        StatusOfTask status = StatusOfTask.IN_PROGRESS;
        int newTaskCount = 0;
        int doneTaskCount = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == StatusOfTask.NEW) {
                newTaskCount += 1;
            } else if (subtask.getStatus() == StatusOfTask.DONE) {
                doneTaskCount += 1;
            }
        }
        if (newTaskCount == subtasks.size()) {
            status = StatusOfTask.NEW;
        } else if (doneTaskCount == subtasks.size()) {
            status = StatusOfTask.DONE;
        }
        super.setStatus(status);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subtasks.size()=" + subtasks.size();
        result = result + '}';
        return result;
    }
}