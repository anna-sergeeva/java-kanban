package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    protected Integer epicId;

    public Subtask(String name, String description, Integer epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, Integer epicId, Duration duration, LocalDateTime startTime) {
        super(id, name, description, duration, startTime);
        this.epicId = epicId;
    }


    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        if (this.epicId == null) {
            this.epicId = epicId;
        }
    }

    @Override
    public TypeOfTask getTaskType() {
        return TypeOfTask.SUBTASK;
    }

    @Override
    public String toString() {
        return "Class.Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}
