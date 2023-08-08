package practicum.yandex.manager;

import practicum.yandex.task.Task;
import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
