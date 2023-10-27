package practicum.yandex.manager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault(String url) {
        return new HttpTaskManager(url);
    }
    public static TaskManager getFileBackedManager(File file) {
        return new FileBackedTasksManager(file);
    }
    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory() { return new InMemoryHistoryManager(); }
}
