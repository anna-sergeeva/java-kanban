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
                node.prev.next = node.next;
                if (node.next == null) {
                    last = node.prev;
                } else {
                    node.next.prev = node.prev;
                }
            } else {
                first = node.next;
                if (first == null) {
                    last = null;
                } else {
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







