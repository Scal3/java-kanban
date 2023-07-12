package practicum.yandex.manager;

import practicum.yandex.task.Task;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    public List<Task> getHistory() {
        return new ArrayList<>(tasksHistory);
    }
}
