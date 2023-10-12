package practicum.yandex.manager.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practicum.yandex.manager.FileBackedTasksManager;
import practicum.yandex.manager.Statuses;
import practicum.yandex.task.EpicTask;
import practicum.yandex.task.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private static final File FILE = new File("./test.csv");

    @BeforeEach
    public void makeNewManager() {
        manager = new FileBackedTasksManager(FILE);
        manager.deleteAllTasks();
        manager.deleteAllEpicTasks();
        manager.deleteAllSubTasks();
    }

    // Get data from file
    @Test
    public void shouldCreateAllTasksFromFileWhenTasksAreInFile() {
        Task task1 = new Task("task1", "task1", Statuses.NEW.name());
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofSeconds(500));

        Task task2 = new Task("task2", "task2", Statuses.NEW.name());
        task2.setStartTime(LocalDateTime.now());
        task2.setDuration(Duration.ofSeconds(500));

        Task task3 = new Task("task3", "task3", Statuses.NEW.name());
        task3.setStartTime(LocalDateTime.now());
        task3.setDuration(Duration.ofSeconds(500));

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        assertArrayEquals(
                manager.getTasksValues().toArray(),
                FileBackedTasksManager.loadFromFile(FILE).getTasksValues().toArray()
        );
    }

    @Test
    public void shouldNotCreateTasksWhenFileIsEmpty() {
        assertEquals(
                0,
                FileBackedTasksManager.loadFromFile(FILE).getTasksValues().size()
        );
    }

    @Test
    public void shouldCreateEpicEvenItWithoutSubtasks() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );

        assertArrayEquals(
                manager.getEpicTasksValues().toArray(),
                FileBackedTasksManager.loadFromFile(FILE).getEpicTasksValues().toArray()
        );
    }

    @Test
    public void shouldBeEmptyHistoryListWhenNoHistoryInFile() {
        assertNull(manager.getHistory());
    }

    // Save in file
    @Test
    public void shouldSaveTasksAllTasksInFile() {
        Task task1 = new Task("task1", "task1", Statuses.NEW.name());
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofSeconds(500));

        Task task2 = new Task("task2", "task2", Statuses.NEW.name());
        task2.setStartTime(LocalDateTime.now());
        task2.setDuration(Duration.ofSeconds(500));

        Task task3 = new Task("task3", "task3", Statuses.NEW.name());
        task3.setStartTime(LocalDateTime.now());
        task3.setDuration(Duration.ofSeconds(500));

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        assertArrayEquals(
                manager.getTasksValues().toArray(),
                FileBackedTasksManager.loadFromFile(FILE).getTasksValues().toArray()
        );
    }

    @Test
    public void shouldNotSaveTasksWhenTasksDoNotExist() {
        manager.createTask(null);
        manager.createTask(null);
        manager.createTask(null);
        assertEquals(0, FileBackedTasksManager.loadFromFile(FILE).getTasksValues().size());
    }
}