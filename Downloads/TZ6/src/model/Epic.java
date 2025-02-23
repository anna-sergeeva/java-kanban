package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskId;


    public Epic(String name, String description, Integer id, ArrayList<Integer> subtaskId) {
        super(name, description, id);
        this.subtaskId = subtaskId;
    }

    public Epic(String name, String description, Integer id) {
        super(name, description, id);
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
