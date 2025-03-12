package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskId;

    public Epic(Integer id, TypeOfTask type, String name, StatusOfTask status, String description, ArrayList<Integer> subtaskId) {
        super(id, type, name, status, description);
        this.subtaskId = subtaskId;
    }

    @Override
    public TypeOfTask getType() {
        return TypeOfTask.EPIC;
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
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

