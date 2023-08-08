package practicum.yandex.util;

// Node for CustomLinkedList in InMemoryHistoryManager
public class Node<T> {
    public Node<T> prev;
    public Node<T> next;
    public T value;

    public Node(Node<T> prev, Node<T> next, T value) {
        this.prev = prev;
        this.next = next;
        this.value = value;
    }

    public Node(T value) {
        this.prev = null;
        this.next = null;
        this.value = value;
    }
}


