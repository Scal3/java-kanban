package practicum.yandex.manager;

import practicum.yandex.task.Task;
import practicum.yandex.util.Node;

import java.util.*;

// Goal
// Избавиться от повторных просмотров в истории
// Если одна и та же задача была добавлена в историю, предыдущая её версия должна перезатираться
// ??? Вроде как ещё и ограничение истории нужно убрать

public class InMemoryHistoryManager implements HistoryManager {
    private static final byte MAX_HISTORY_QUANTITY = 10;
    private static final byte FIRST_ELEMENT = 0;
    private final List<Task> tasksHistory;

    public InMemoryHistoryManager() {
        tasksHistory = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (tasksHistory.size() < MAX_HISTORY_QUANTITY) {
            tasksHistory.add(task);
        } else {
            tasksHistory.remove(FIRST_ELEMENT);
            tasksHistory.add(task);
        }
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(tasksHistory);
    }
}


// Нельзя делать удаление предыдущей записи через итерацию по листу
// Предыдущий просмотр должен быть удалён сразу же после появления нового — за O(1)
// CustomLinkedList позволяет удалить элемент из произвольного места за О(1) с одним важным условием —
// если программа уже дошла до этого места по списку(Что это значит???)
class CustomLinkedList {
    private Node<Task> head;
    private Node<Task> tail;
    private int size;
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
        size++;
    }

    public void removeNode(Node<Task> node) {
        //
    }

    public List<Task> getTasks() {
        if (head == null) return null;

        List<Task> list = new ArrayList<>();
        Node<Task> node = head;
        int counter = size;

        while (counter > 0) {
            list.add(node.value);
            node = node.next;
            counter--;
        }

        return list;
    }
}