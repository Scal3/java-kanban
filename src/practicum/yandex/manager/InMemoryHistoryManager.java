package practicum.yandex.manager;

import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;
import practicum.yandex.util.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList tasksHistory = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (tasksHistory.contains(task.getId())) {
            tasksHistory.removeNode(tasksHistory.getById(task.getId()));
            tasksHistory.linkLast(task);
        } else {
            tasksHistory.linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (!tasksHistory.contains(id)) return;

        if (tasksHistory.getById(id).value instanceof EpicTask) {
            EpicTask epic = (EpicTask) tasksHistory.getById(id).value;

            for (SubTask sub : epic.getSubtasks()) {
                tasksHistory.removeNode(tasksHistory.getById(sub.getId()));
            }
        }

        tasksHistory.removeNode(tasksHistory.getById(id));
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory.getTasks();
    }
}

class CustomLinkedList {
    private Node<Task> head;
    private Node<Task> tail;
    private final Map<Integer, Node<Task>> map = new HashMap<>();

    public void linkLast(Task task) {
        if (task == null) return;

        Node<Task> newNode = new Node<>(task);

        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }

        tail = newNode;
        map.put(task.getId(), newNode);
    }

    public void removeNode(Node<Task> node) {
        if (node == null) return;

        if (node.prev != null) {
            node.prev.next = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        }

        map.remove(node.value.getId());
    }

    public Node<Task> getById(int id) {
        return map.get(id);
    }

    public boolean contains(int id) {
        return map.containsKey(id);
    }

    public List<Task> getTasks() {
        if (head == null) return null;

        List<Task> list = new ArrayList<>();

        for (Node<Task> node : map.values()) {
            list.add(node.value);
        }

        return list;
    }
}