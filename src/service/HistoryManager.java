package service;

import model.Task;
import java.util.ArrayList;

public interface HistoryManager {

    ArrayList<Task> getHistory();

    void add(Task task);

    void remove(int id);  // добавила

}