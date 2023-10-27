import practicum.yandex.manager.Managers;
import practicum.yandex.manager.TaskManager;
import practicum.yandex.server.KVServer;
import practicum.yandex.task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) throws IOException {
        Task task = new Task("task", "task", "NEW");
        task.setStartTime(LocalDateTime.of(2023, Month.NOVEMBER, 29, 10, 20));
        task.setDuration(Duration.ofSeconds(5000));

        Task task2 = new Task("task2", "task2", "NEW");
        task2.setStartTime(LocalDateTime.now());
        task2.setDuration(Duration.ofSeconds(50000));

        new KVServer().start();

        TaskManager manager = Managers.getDefault("http://localhost:8078/");
        manager.createTask(task);
        manager.createTask(task2);

        System.out.println(manager.getTasksValues());
    }
}
