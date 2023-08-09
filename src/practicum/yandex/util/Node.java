package practicum.yandex.util;

import practicum.yandex.task.Task;

// Node for CustomLinkedList in InMemoryHistoryManager
public class Node {
    private Node prev;
    private Node next;
    private Task value;

    public Node(Node prev, Node next, Task value) {
        this.prev = prev;
        this.next = next;
        this.value = value;
    }

    public Node(Task value) {
        this.prev = null;
        this.next = null;
        this.value = value;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Task getValue() {
        return value;
    }
}


