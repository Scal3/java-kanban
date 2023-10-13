import practicum.yandex.manager.InMemoryTaskManager;
import practicum.yandex.manager.Statuses;
import practicum.yandex.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("task1", "task1", Statuses.NEW.name());
        task1.setStartTime(LocalDateTime.of(2020, Month.JANUARY, 1, 10, 0));
        task1.setDuration(Duration.ofSeconds(2000));

        Task task2 = new Task("task2", "task2", Statuses.NEW.name());
        task2.setStartTime(LocalDateTime.of(2021, Month.JANUARY, 1, 10, 0));
        task2.setDuration(Duration.ofSeconds(2000));

        Task task3 = new Task("task3", "task3", Statuses.NEW.name());
        task3.setStartTime(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 0));
        task3.setDuration(Duration.ofSeconds(2000));

        Task task4 = new Task("task4", "task4", Statuses.NEW.name());

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createTask(task4);


        System.out.println(manager.getPrioritizedTasks());
    }
}
