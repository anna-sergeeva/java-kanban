package model;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Integer id;
    private StatusOfTask status;
    private TypeOfTask type;


    public Task(String name, String description, StatusOfTask status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = 0;
    }

    //конструктор для создания измененных задач
    public Task(String name, String description, StatusOfTask status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    //конструктор для создания нового эпика
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = 0;
    }

    //конструктор для создания измененного эпика
    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public TypeOfTask getType() {
        return TypeOfTask.TASK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StatusOfTask getStatus() {
        return status;
    }

    public void setStatus(StatusOfTask status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
