package service;

import exceptions.ManagerSaveException;
import model.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    private int idOfTasksCounter = 1;


    public FileBackedTaskManager(File file) {
        this.file = file;
    }


    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                Task task = TaskFormatter.fromString(line);
                if (task.getId() > fileBackedTaskManager.idOfTasksCounter) {
                    fileBackedTaskManager.idOfTasksCounter = task.getId();
                }
                switch (task.getTaskType()) {
                    case TASK:
                        fileBackedTaskManager.tasks.put(task.getId(), task);
                        break;
                    case SUBTASK:
                        fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
                        break;
                    case EPIC:
                        fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                        break;
                }
            }
            for (Subtask subtask : fileBackedTaskManager.subtasks.values()) {
                Epic epic = fileBackedTaskManager.epics.get(subtask.getEpicId());
                epic.getSubtaskId().add(subtask.getId());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при чтении файла");
        }
        return fileBackedTaskManager;
    }

    private void save() {
        Path path = file.toPath();
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (Exception e) {
                throw new ManagerSaveException("Произошла ошибка при создании файла");
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getListOfTasks()) {
                writer.write(TaskFormatter.toString(task) + "\n");
            }
            for (Epic epic : getListOfEpics()) {
                writer.write(TaskFormatter.toString(epic) + "\n");
            }
            for (Subtask subtask : getListOfSubtasks()) {
                writer.write(TaskFormatter.toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении данных в файл");
        }
    }

    @Override
    public int addNewTask(Task task) {
        final int id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        final int id = super.addNewSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(Integer id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(Integer idSub) {
        super.removeSubtaskById(idSub);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }
}