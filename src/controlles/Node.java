package controlles;

public class Node<T> {
    Node<T> next;
    Node<T> last;
    T task;

    public Node(Node<T> last, T task, Node<T> next) {
        this.next = next;
        this.task = task;
        this.last = last;
    }
}
