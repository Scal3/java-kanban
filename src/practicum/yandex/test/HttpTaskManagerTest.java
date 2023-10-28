package practicum.yandex.test;

import org.junit.jupiter.api.*;
import practicum.yandex.manager.HttpTaskManager;
import practicum.yandex.manager.Statuses;
import practicum.yandex.server.KVServer;
import practicum.yandex.task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private static final KVServer server;

    static {
        try {
            server = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void startServer() {
        server.start();
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @BeforeEach
    @Override
    public void makeManager() {
        manager = new HttpTaskManager("http://localhost:8078/");
    }

    // Get data from server
    @Test
    public void shouldCreateAllTasksFromServer() {
        server.setTestData();
        assertEquals(2, manager.getTasksValues().size());
    }

    @Test
    public void shouldNotCreateTasksWhenServerIsEmpty() {
        assertEquals(0, manager.getTasksValues().size());
    }

    // save data to server
    @Test
    public void shouldSaveTasksAllTasksToServer() {
        Task task1 = new Task("task1", "task1", Statuses.NEW.name());
        task1.setStartTime(LocalDateTime.of(2023, Month.APRIL, 10, 10, 10));
        task1.setDuration(Duration.ofHours(20));

        Task task2 = new Task("task2", "task2", Statuses.NEW.name());
        task2.setStartTime(LocalDateTime.of(2022, Month.APRIL, 10, 10, 10));
        task2.setDuration(Duration.ofHours(20));

        manager.createTask(task1);
        manager.createTask(task2);
        assertEquals(2, manager.getTasksValues().size());
    }

    @Test
    public void shouldNotSaveTasksWhenTasksDoNotExist() {
        assertEquals(0, manager.getTasksValues().size());
    }
}