package practicum.yandex.manager.test;

import org.junit.jupiter.api.Test;
import practicum.yandex.manager.HistoryManager;
import practicum.yandex.manager.Statuses;
import practicum.yandex.task.Task;

import static org.junit.jupiter.api.Assertions.*;

public abstract class HistoryManagerTest<T extends HistoryManager> {
    T manager;

    // add
    @Test
    public void shouldAddTaskToHistoryWhenDataIsCorrect() {
        Task task = new Task("task", "task", Statuses.NEW.name());
        task.setId(1);
        manager.add(task);
        assertTrue(manager.getHistory().contains(task));
    }

    @Test
    public void shouldNotAddTaskToHistoryWhenDataIsNotCorrect() {
        manager.add(null);
        assertNull(manager.getHistory());
    }

    @Test
    public void shouldReplaceTaskWhenItIsAlreadyInside() {
        Task task = new Task("task", "task", Statuses.NEW.name());
        task.setId(1);
        manager.add(task);
        manager.add(task);
        assertTrue(manager.getHistory().contains(task));
        assertEquals(1, manager.getHistory().size());
    }

    // remove
    @Test
    public void shouldRemoveTaskWhenTaskExists() {
        Task task = new Task("task", "task", Statuses.NEW.name());
        task.setId(1);
        manager.add(task);
        manager.remove(1);
        assertFalse(manager.getHistory().contains(task));
    }

    @Test
    public void shouldNotRemoveTaskWhenTaskIdIsNotCorrect() {
        Task task = new Task("task", "task", Statuses.NEW.name());
        task.setId(1);
        manager.add(task);
        manager.remove(10);
        assertTrue(manager.getHistory().contains(task));
    }

    @Test
    public void shouldRemoveTaskFromHistoryBeginning() {
        Task task1 = new Task("task1", "task1", Statuses.NEW.name());
        task1.setId(1);

        Task task2 = new Task("task2", "task2", Statuses.NEW.name());
        task2.setId(2);

        Task task3 = new Task("task3", "task3", Statuses.NEW.name());
        task3.setId(3);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(1);

        assertFalse(manager.getHistory().contains(task1));
        assertTrue(manager.getHistory().contains(task2));
        assertTrue(manager.getHistory().contains(task3));
    }

    @Test
    public void shouldRemoveTaskFromHistoryMiddle() {
        Task task1 = new Task("task1", "task1", Statuses.NEW.name());
        task1.setId(1);

        Task task2 = new Task("task2", "task2", Statuses.NEW.name());
        task2.setId(2);

        Task task3 = new Task("task3", "task3", Statuses.NEW.name());
        task3.setId(3);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(2);

        assertFalse(manager.getHistory().contains(task2));
        assertTrue(manager.getHistory().contains(task1));
        assertTrue(manager.getHistory().contains(task3));
    }

    @Test
    public void shouldRemoveTaskFromHistoryEnding() {
        Task task1 = new Task("task1", "task1", Statuses.NEW.name());
        task1.setId(1);

        Task task2 = new Task("task2", "task2", Statuses.NEW.name());
        task2.setId(2);

        Task task3 = new Task("task3", "task3", Statuses.NEW.name());
        task3.setId(3);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(3);

        assertFalse(manager.getHistory().contains(task3));
        assertTrue(manager.getHistory().contains(task1));
        assertTrue(manager.getHistory().contains(task2));
    }

    // getHistory
    @Test
    public void shouldReturnHistoryWhenTasksWereCalled() {
        Task task = new Task("task", "task", Statuses.NEW.name());
        task.setId(1);
        manager.add(task);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void shouldReturnNullWhenHistoryIsEmpty() {
        assertNull(manager.getHistory());
    }
}