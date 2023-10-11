package practicum.yandex.manager;

import practicum.yandex.task.Task;
import practicum.yandex.util.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList tasksHistory = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (task == null || task.getId() == null) return;

        if (tasksHistory.contains(task.getId())) {
            tasksHistory.removeNode(tasksHistory.getById(task.getId()));
        }

        tasksHistory.linkLast(task);
    }

    @Override
    public void remove(int id) {
        tasksHistory.removeNode(tasksHistory.getById(id));
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory.getTasks();
    }
}

class CustomLinkedList {
    private Node head;
    private Node tail;
    private final Map<Integer, Node> map = new HashMap<>();

    public void linkLast(Task task) {
        if (task == null) return;

        Node newNode = new Node(task);

        if (tail == null) {
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
        }

        tail = newNode;
        map.put(task.getId(), newNode);
    }

    public void removeNode(Node node) {
        if (node == null) return;

        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        }

        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        }

        map.remove(node.getValue().getId());
    }

    public Node getById(int id) {
        return map.get(id);
    }

    public boolean contains(int id) {
        return map.containsKey(id);
    }

    public List<Task> getTasks() {
        if (head == null) return null;

        List<Task> list = new ArrayList<>();

        for (Node node : map.values()) {
            list.add(node.getValue());
        }

        return list;
    }
}