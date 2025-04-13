package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task> {
    protected String name;
    protected String description;
    protected Integer id;
    protected StatusOfTask status;
    protected Duration duration = Duration.ofMinutes(0);
    protected LocalDateTime startTime;


    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = StatusOfTask.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description) {
        this(name, description, Duration.ofMinutes(0), null);
    }

    public Task(Integer id, String name, String description) {
        this(id, name, description, Duration.ofMinutes(0), null);
    }

    public Task(Integer id, String name, String description, Duration duration, LocalDateTime startTime) {
        this(name, description, duration, startTime);
        this.id = id;
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
        if (this.id == null) {
            this.id = id;
        }
    }

    public StatusOfTask getStatus() {
        return status;
    }

    public void setStatus(StatusOfTask status) {
        this.status = status;
    }

    public Duration getDuration() {
        if (duration == null) {
            return Duration.ofMinutes(0);
        }
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plusMinutes(duration.toMinutes());
    }

    public TypeOfTask getTaskType() {
        return TypeOfTask.TASK;
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
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public int compareTo(Task o) {
        return this.startTime.compareTo(o.startTime);
    }

}
