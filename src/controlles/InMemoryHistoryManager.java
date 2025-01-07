package controlles;

import model.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    Node headNode;
    Node lastNode;
    private ArrayList<Task> taskHistory = new ArrayList<>();
    public HashMap<Integer, Node<Task>> historyHashMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public ArrayList<Task> getHistoryTask() {
        taskHistory.clear();
        for (Node node : historyHashMap.values()) {
            taskHistory.add((Task) node.task);
        }
        return taskHistory;
    }

    @Override
    public void remove(int id) {
        Node nodeToDelete = historyHashMap.get(id);
        if (nodeToDelete == null) {
            return;
        }
        historyHashMap.remove(id);
        removeNode(nodeToDelete);
    }

    public void linkLast(Task task) { //добавляет задачу в конец списка
        Node oldLast = lastNode;
        Node newNode = new Node(lastNode, task, null);
        historyHashMap.put(task.getId(), newNode);
        lastNode = newNode;
        if (oldLast == null) {
            headNode = lastNode;
        } else {
            oldLast.next = newNode;
        }
    }

    public void removeNode(Node node) {
        Node prev = node.last;
        Node next = node.next;
        node.task = null;

        if (prev != null) {
            prev.next = next;
        } else {
            headNode = next;
        }

        if (next != null) {
            next.last = prev;
        } else {
            lastNode = prev;
        }
    }
}
