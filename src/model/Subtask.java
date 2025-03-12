package model;

public class Subtask extends Task {
    private final Epic epic;

    public Subtask(String name, String description, StatusOfTask status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
    }

    public Subtask(String name, String description, StatusOfTask status, Epic epic, int id) {
        super(name, description, status, id);
        this.epic = epic;
    }

    @Override
    public TypeOfTask getType() {
        return TypeOfTask.SUBTASK;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", epic=" + getEpic() +
                '}';
    }
}