package service;
import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;


    private void linkLast(Task task) {
        final Node node = new Node(task, last, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }

    @Override
    public void remove(int id) {
        final Node node = nodeMap.remove(id);
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next; //тут может записаться как null так и ссылка на следующую ноду, таким образом мы избавляемся от необходимости снова возвращаться к этой ссылке
                if (node.next == null) { //если же дальше больше нет нод, то мы делаем предыдущую ноду последней
                    last = node.prev;
                } else {  // если нода есть, даем ей ссылку на предыдущую ноду
                    node.next.prev = node.prev;
                }
            } else { // node == first, остался случай когда мы удаляем первую ноду
                first = node.next; //присваиваем первой ноде ссылку на следующую ноду, если следующей ноды нет, то присвоится null
                if (first == null) { //если была удалена единственная нода, обнуляем last
                    last = null;
                } else { //если нода не единственная - обнуляем ссылку на предыдущую ноду
                    first.prev = null;
                }
            }
        }
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = first;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());
        linkLast(task);
        nodeMap.put(task.getId(), last);
    }

}







