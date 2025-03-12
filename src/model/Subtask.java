package model;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(Integer id, TypeOfTask type, String name, StatusOfTask status, String description, Integer epicId) {
        super(id, type, name, status, description);
        this.epicId = epicId;
    }

    @Override
    public TypeOfTask getType() {
        return TypeOfTask.SUBTASK;
    }


    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }



    @Override
    public String toString() {
        return "model.Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}
