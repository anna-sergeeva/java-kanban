package model;

import java.util.ArrayList;


public class Epic extends Task {
    protected ArrayList<Integer> subtaskId;

    public Epic(String name, String description) {
        super(name, description);
        subtaskId = new ArrayList<>();
    }

    public Epic(Integer id, String name, String description) {
        super(id, name, description);
        subtaskId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    @Override
    public TypeOfTask getTaskType() {
        return TypeOfTask.EPIC;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    public void addSubtaskId(Integer id) {
        subtaskId.add(id);
    }

    public void clearSubtaskId() {
        subtaskId.clear();
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subtaskId=" + subtaskId +
                '}';
    }
}

