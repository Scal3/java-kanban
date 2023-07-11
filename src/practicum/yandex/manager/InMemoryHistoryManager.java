package practicum.yandex.manager;

import practicum.yandex.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static final byte MAX_HISTORY_QUANTITY = 10;
    private final Map<Integer, Task> tasksHistory;
    private int historyTasksCounter;

    public InMemoryHistoryManager() {
        tasksHistory = new HashMap<>();
        historyTasksCounter = 0;
    }

    @Override
    public void add(Task task) {
        tasksHistory.put(++historyTasksCounter, task);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();

        if (historyTasksCounter <= MAX_HISTORY_QUANTITY) {
            for (int i = historyTasksCounter; i > 0; i--) {
                history.add(tasksHistory.get(i));
            }
        } else {
            for (int i = historyTasksCounter; i > historyTasksCounter - MAX_HISTORY_QUANTITY; i--) {
                history.add(tasksHistory.get(i));
            }
        }

        return history;
    }
}
