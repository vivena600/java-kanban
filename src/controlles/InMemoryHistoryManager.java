package controlles;

import model.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryHistoryManager implements HistoryManager {
    private Node headNode;
    private Node lastNode;
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
        List<Task> taskHistory  = historyHashMap.values().stream()
                .map(node -> node.task)
                .collect(Collectors.toList());
        return new ArrayList<>(taskHistory);
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

    private void linkLast(Task task) { //добавляет задачу в конец списка
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

    private void removeNode(Node node) {
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
