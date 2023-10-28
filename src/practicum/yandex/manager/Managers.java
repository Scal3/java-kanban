package practicum.yandex.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import practicum.yandex.adapter.DurationAdapter;
import practicum.yandex.adapter.LocalDateTimeAdapter;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

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
    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}
