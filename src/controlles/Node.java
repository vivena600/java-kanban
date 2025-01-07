package controlles;
import model.Task;

public class Node<Task> {
    Node<Task> next;
    Node<Task> last;
    Task task;

    public Node(Node<Task> last, Task task,Node<Task> next){
        this.next = next;
        this.task = task;
        this.last = last;
    }
}
