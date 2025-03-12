package service;

import exceptions.ManagerLoadFromFileException;
import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    protected void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(toString(subtask) + "\n");
            }
            writer.write("\n");
            for (Task task : getHistory()) {
                writer.write(task.getId() + ",");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении данных", e);
        }
    }

    private String toString(Task task) {
        String taskString;
        taskString = String.format("%s,%s,%s,%s,%s,",
                task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription());
        if (task.getType() == TypeOfTask.SUBTASK) {
            taskString = taskString + String.format("%s", ((Subtask) task).getEpic());
        }
        return taskString;
    }


    public static FileBackedTaskManager loadFromFile(File file) throws ManagerLoadFromFileException {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            String contents = Files.readString(file.toPath());
            String[] splits = contents.split("\n\n");
            if (splits.length == 2) {
                String[] tasksValues = splits[0].split("\n");
                String[] historyValues = splits[1].split(",");
                loadTasks(taskManager, tasksValues);
                loadHistory(taskManager, historyValues);
            }
        } catch (IOException ex) {
            throw new ManagerLoadFromFileException("Произошла ошибка при чтении из файла", ex);
        }

        return taskManager;
    }


    private static void loadTasks(FileBackedTaskManager taskManager, String[] tasksValues) {
        boolean isHeader = true;
        for (String taskString : tasksValues) {
            if (!isHeader) {
                Task task = taskFromString(taskString, taskManager);
                if (task != null) {
                    taskManager.addTaskToMap(task);
                    taskManager.id = Integer.max(task.getId(), taskManager.id);
                }
            }
            isHeader = false;
        }
    }

    private void addTaskToMap(Task task) {
        TypeOfTask type = task.getType();
        if (type == TypeOfTask.TASK) {
            tasks.put(task.getId(), task);
        } else if (type == TypeOfTask.EPIC) {
            epics.put(task.getId(), (Epic) task);
        } else if (type == TypeOfTask.SUBTASK) {
            subtasks.put(task.getId(), (Subtask) task);
            Epic epic = ((Subtask) task).getEpic();
            epic.addSubtaskToEpic((Subtask) task);
            epic.updateStatus();
        }
    }

    private static Task taskFromString(String value, TaskManager taskManager) throws ManagerLoadFromFileException {
        final String[] values = value.split(",");
        if (values.length < 5) {
            throw new ManagerLoadFromFileException("Произошла ошибка при чтении строки задачи");
        }
        final int id = Integer.parseInt(values[0]);
        final TypeOfTask type = TypeOfTask.valueOf(values[1]);
        if (type == TypeOfTask.TASK) {
            return new Task(values[2], values[4], StatusOfTask.valueOf(values[3]), id);
        } else if (type == TypeOfTask.EPIC) {
            return new Epic(values[2], values[4], id);
        } else if (type == TypeOfTask.SUBTASK) {
            if (values.length < 6) {
                throw new ManagerLoadFromFileException("Произошла ошибка при чтении строки подзадачи");
            }
            final int epicId = Integer.parseInt(values[5]);
            return new Subtask(values[2], values[4], StatusOfTask.valueOf(values[3]), taskManager.getEpicById(epicId), id);
        }
        return null;
    }


    private static void loadHistory(FileBackedTaskManager taskManager, String[] historyValues) {
        //очищаем историю
        for (Task task : taskManager.getHistory()) {
            taskManager.historyManager.remove(task.getId());
        }
        for (String value : historyValues) {
            final int id = Integer.parseInt(value);
            if (taskManager.tasks.containsKey(id)) {
                taskManager.historyManager.add(taskManager.getTaskById(id));
            } else if (taskManager.epics.containsKey(id)) {
                taskManager.historyManager.add(taskManager.getEpicById(id));
            } else if (taskManager.subtasks.containsKey(id)) {
                taskManager.historyManager.add(taskManager.getSubtaskById(id));
            }
        }
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
    public Task getTaskById(Integer id) {
        final Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        final Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicById(Integer id) {
        final Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    //создание новых задач
    @Override
    public int addTask(Task task) {
        final int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public int addEpic(Epic task) {
        final int id = super.addEpic(task);
        save();
        return id;
    }

    @Override
    public int addSubtask(Subtask task) {
        final int id = super.addSubtask(task);
        save();
        return id;
    }

    //обновление задач
    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateSubtask(Subtask newTask) {
        super.updateSubtask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }
}