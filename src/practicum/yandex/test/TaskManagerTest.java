package practicum.yandex.test;

import org.junit.jupiter.api.Test;
import practicum.yandex.manager.Statuses;
import practicum.yandex.manager.TaskManager;
import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    T manager;

    public abstract void makeManager();

    // getHistory
    @Test
    public void shouldReturnHistoryWhenTwoTasksWereCalled() {
        Task[] tasks = new Task[]{
                new Task("task1", "task1", Statuses.NEW.name()),
                new Task("task2", "task2", Statuses.NEW.name())
        };
        manager.createTask(tasks[0]);
        manager.createTask(tasks[1]);
        manager.getTaskById(1);
        manager.getTaskById(2);

        assertArrayEquals(tasks, manager.getHistory().toArray());
    }

    @Test
    public void shouldReturnNullHistoryWhenNoTasksWereCalled() {
        assertNull(manager.getHistory());
    }

    @Test
    public void shouldReturnNullHistoryWhenTaskWasCalledWithWrongId() {
        manager.getTaskById(10);
        manager.getTaskById(-10);
        assertNull(manager.getHistory());
    }

    // getTasksValues
    @Test
    public void shouldReturnTasksWhenTheyWereAdded() {
        Task[] tasks = new Task[]{
                new Task("task1", "task1", Statuses.NEW.name()),
                new Task("task2", "task2", Statuses.NEW.name())
        };
        manager.createTask(tasks[0]);
        manager.createTask(tasks[1]);
        assertArrayEquals(tasks, manager.getTasksValues().toArray());
    }

    @Test
    public void shouldReturnNothingWhenNoTasksWereAdded() {
        assertArrayEquals(new Task[]{}, manager.getTasksValues().toArray());
    }

    // getEpicTasksValues
    @Test
    public void shouldReturnEpicTasksWhenTheyWereAdded() {
        EpicTask[] tasks = new EpicTask[]{
                new EpicTask("epic1", "epic1", Statuses.NEW.name(), Collections.emptyList()),
                new EpicTask("epic2", "epic2", Statuses.NEW.name(), Collections.emptyList())
        };
        manager.createEpicTask(tasks[0]);
        manager.createEpicTask(tasks[1]);
        assertArrayEquals(tasks, manager.getEpicTasksValues().toArray());
    }

    @Test
    public void shouldReturnNothingWhenNoEpicTasksWereAdded() {
        assertArrayEquals(new EpicTask[]{}, manager.getEpicTasksValues().toArray());
    }

    // getSubTasksValues
    @Test
    public void shouldReturnSubTasksWhenTheyWereAdded() {
        manager.createEpicTask(new EpicTask(
                "epic", "epic", Statuses.NEW.name(), Collections.emptyList()
        ));

        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setDuration(Duration.ofSeconds(100));
        sub1.setStartTime(LocalDateTime.now());

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.NEW.name(), 1);
        sub2.setDuration(Duration.ofSeconds(100));
        sub2.setStartTime(LocalDateTime.now());

        SubTask[] tasks = new SubTask[]{ sub1, sub2 };
        manager.createSubTask(tasks[0]);
        manager.createSubTask(tasks[1]);
        assertArrayEquals(tasks, manager.getSubTasksValues().toArray());
    }

    @Test
    public void shouldReturnNothingWhenNoSubTasksWereAdded() {
        assertArrayEquals(new SubTask[]{}, manager.getSubTasksValues().toArray());
    }

    // deleteAllTasks
    @Test
    public void shouldBeEmptyTasksListAfterDeletingAllTasks() {
        manager.createTask(new Task("task1", "task1", Statuses.NEW.name()));
        manager.createTask(new Task("task2", "task2", Statuses.NEW.name()));
        manager.deleteAllTasks();
        assertArrayEquals(new Task[]{}, manager.getTasksValues().toArray());
    }

    @Test
    public void shouldBeEmptyTasksListAfterDeletingAllTasksButNoTasksWereAdded() {
        manager.deleteAllTasks();
        assertArrayEquals(new Task[]{}, manager.getTasksValues().toArray());
    }

    // deleteAllEpicTasks
    @Test
    public void shouldBeEmptyEpicTasksListAfterDeletingAllEpicTasks() {
        manager.createEpicTask(
                new EpicTask("epic1", "epic1", Statuses.NEW.name(), Collections.emptyList())
        );
        manager.createEpicTask(
                new EpicTask("epic2", "epic2", Statuses.NEW.name(), Collections.emptyList())
        );
        manager.deleteAllEpicTasks();
        assertArrayEquals(new EpicTask[]{}, manager.getEpicTasksValues().toArray());
    }

    @Test
    public void shouldBeEmptyEpicTasksListAfterDeletingAllEpicTasksButNoEpicTasksWereAdded() {
        manager.deleteAllEpicTasks();
        assertArrayEquals(new EpicTask[]{}, manager.getEpicTasksValues().toArray());
    }

    // deleteAllSubTasks
    @Test
    public void shouldBeEmptySubTasksListAfterDeletingAllSubTasks() {
        manager.createSubTask(new SubTask("sub1", "sub1", Statuses.NEW.name(), 0));
        manager.createSubTask(new SubTask("sub2", "sub2", Statuses.NEW.name(), 0));
        manager.deleteAllSubTasks();
        assertArrayEquals(new SubTask[]{}, manager.getSubTasksValues().toArray());
    }

    @Test
    public void shouldBeEmptySubTasksListAfterDeletingAllSubTasksButNoSubTasksWereAdded() {
        manager.deleteAllSubTasks();
        assertArrayEquals(new SubTask[]{}, manager.getSubTasksValues().toArray());
    }

    // getTaskById
    @Test
    public void shouldReturnTaskWhenIdIsCorrect() {
        Task task = new Task("task", "task", Statuses.NEW.name());
        manager.createTask(task);
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    public void shouldReturnNullWhenNoTasksWereAdded() {
        assertNull(manager.getTaskById(1));
    }

    @Test
    public void shouldReturnNullWhenTaskWasCalledWithWrongId() {
        manager.createTask(new Task("task", "task", Statuses.NEW.name()));
        assertNull(manager.getTaskById(10));
    }

    // getEpicTaskById
    @Test
    public void shouldReturnEpicTaskWhenIdIsCorrect() {
        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList());
        manager.createEpicTask(epic);
        assertEquals(epic, manager.getEpicTaskById(1));
    }

    @Test
    public void shouldReturnNullWhenNoEpicTasksWereAdded() {
        assertNull(manager.getEpicTaskById(1));
    }

    @Test
    public void shouldReturnNullWhenEpicTaskWasCalledWithWrongId() {
        manager.createTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );
        assertNull(manager.getEpicTaskById(10));
    }

    // getSubTaskById
    @Test
    public void shouldReturnSubTaskWhenIdIsCorrect() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );

        SubTask sub = new SubTask("sub", "sub", Statuses.NEW.name(), 1);
        sub.setDuration(Duration.ofSeconds(100));
        sub.setStartTime(LocalDateTime.now());

        manager.createSubTask(sub);
        assertEquals(sub, manager.getSubTaskById(2));
    }

    @Test
    public void shouldReturnNullWhenNoSubTasksWereAdded() {
        assertNull(manager.getSubTaskById(1));
    }

    @Test
    public void shouldReturnNullWhenSubTaskWasCalledWithWrongId() {
        manager.createSubTask(new SubTask("sub", "sub", Statuses.NEW.name(), 0));
        assertNull(manager.getSubTaskById(10));
    }

    // createTask
    @Test
    public void shouldCreateTaskWhenDataIsCorrect() {
        Task task = new Task("task", "task", Statuses.NEW.name());
        manager.createTask(task);
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    public void shouldNotCreateTaskWhenDataIsNull() {
        manager.createTask(null);
        assertNull(manager.getTaskById(1));
    }

    // createEpicTask
    @Test
    public void shouldCreateEpicTaskWhenDataIsCorrect() {
        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList());
        manager.createEpicTask(epic);
        assertEquals(epic, manager.getEpicTaskById(1));
    }

    @Test
    public void shouldNotCreateEpicTaskWhenDataIsNull() {
        manager.createEpicTask(null);
        assertNull(manager.getEpicTaskById(1));
    }

    // createSubTask
    @Test
    public void shouldCreateSubTaskWhenDataIsCorrect() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );

        SubTask sub = new SubTask("sub", "sub", Statuses.NEW.name(), 1);
        sub.setDuration(Duration.ofSeconds(100));
        sub.setStartTime(LocalDateTime.now());

        manager.createSubTask(sub);
        assertEquals(sub, manager.getSubTaskById(2));
    }

    @Test
    public void shouldNotCreateSubTaskWhenDataIsNull() {
        manager.createSubTask(null);
        assertNull(manager.getSubTaskById(1));
    }

    // updateTask
    @Test
    public void shouldUpdateTaskWhenTaskExists() {
        manager.createTask(new Task("task", "task", Statuses.NEW.name()));
        Task updatedTask = manager.getTaskById(1);
        updatedTask.setName("updated");
        updatedTask.setDescription("updated");
        manager.updateTask(updatedTask);
        assertEquals(updatedTask, manager.getTaskById(1));
    }

    @Test
    public void shouldDoNothingWhenTaskForUpdatingIsNotCreated() {
        manager.updateTask(new Task("task", "task", Statuses.NEW.name()));
        assertNull(manager.getTaskById(1));
    }

    // updateEpicTask
    @Test
    public void shouldUpdateEpicTaskWhenEpicTaskExists() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );
        EpicTask updatedEpicTask = manager.getEpicTaskById(1);
        updatedEpicTask.setName("updated");
        updatedEpicTask.setDescription("updated");
        manager.updateEpicTask(updatedEpicTask);
        assertEquals(updatedEpicTask, manager.getEpicTaskById(1));
    }

    @Test
    public void shouldDoNothingWhenEpicTaskForUpdatingIsNotCreated() {
        manager.updateEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );
        assertNull(manager.getEpicTaskById(1));
    }

    // updateSubTask
    @Test
    public void shouldUpdateSubTaskWhenSubTaskExists() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );

        SubTask sub = new SubTask("sub", "sub", Statuses.NEW.name(), 1);
        sub.setDuration(Duration.ofSeconds(100));
        sub.setStartTime(LocalDateTime.now());

        manager.createSubTask(sub);

        SubTask updatedSubTask = manager.getSubTaskById(2);
        updatedSubTask.setName("updated");
        updatedSubTask.setDescription("updated");

        manager.updateSubTask(updatedSubTask);
        assertEquals(updatedSubTask, manager.getSubTaskById(2));
    }

    @Test
    public void shouldDoNothingWhenSubTaskForUpdatingIsNotCreated() {
        manager.updateSubTask(new SubTask("sub", "sub", Statuses.NEW.name(), 0));
        assertNull(manager.getSubTaskById(1));
    }

    // deleteTaskById
    @Test
    public void shouldDeleteTaskWhenTaskIdIsCorrect() {
        manager.createTask(new Task("task", "task", Statuses.NEW.name()));
        manager.deleteTaskById(1);
        assertNull(manager.getTaskById(1));
    }

    @Test
    public void shouldDoNothingWhenTasksListIsEmpty() {
        manager.deleteTaskById(1);
        assertArrayEquals(new Task[]{}, manager.getTasksValues().toArray());
    }

    @Test
    public void shouldNotDeleteTaskWhenTaskIdIsNotCorrect() {
        manager.createTask(new Task("task", "task", Statuses.NEW.name()));
        Task task = manager.getTaskById(1);
        manager.deleteTaskById(10);
        assertEquals(task, manager.getTaskById(1));
    }

    // deleteEpicTaskById
    @Test
    public void shouldDeleteEpicTaskWhenEpicTaskIdIsCorrect() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );
        manager.deleteEpicTaskById(1);
        assertNull(manager.getEpicTaskById(1));
    }

    @Test
    public void shouldDoNothingWhenEpicTasksListIsEmpty() {
        manager.deleteEpicTaskById(1);
        assertArrayEquals(new EpicTask[]{}, manager.getEpicTasksValues().toArray());
    }

    @Test
    public void shouldNotDeleteEpicTaskWhenEpicTaskIdIsNotCorrect() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );
        EpicTask epic = manager.getEpicTaskById(1);
        manager.deleteEpicTaskById(10);
        assertEquals(epic, manager.getEpicTaskById(1));
    }

    // deleteSubTaskById
    @Test
    public void shouldDeleteSubTaskWhenSubTaskIdIsCorrect() {
        manager.createSubTask(new SubTask("sub", "sub", Statuses.NEW.name(), 0));
        manager.deleteSubTaskById(1);
        assertNull(manager.getSubTaskById(1));
    }

    @Test
    public void shouldDoNothingWhenSubTasksListIsEmpty() {
        manager.deleteSubTaskById(1);
        assertArrayEquals(new SubTask[]{}, manager.getSubTasksValues().toArray());
    }

    @Test
    public void shouldNotDeleteSubTaskWhenSubTaskIdIsNotCorrect() {
        manager.createSubTask(new SubTask("sub", "sub", Statuses.NEW.name(), 0));
        SubTask sub = manager.getSubTaskById(1);
        manager.deleteSubTaskById(10);
        assertEquals(sub, manager.getSubTaskById(1));
    }

    // getEpicSubTasks
    @Test
    public void shouldReturnAllEpicsSubtasksWhenTheyExist() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );

        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setDuration(Duration.ofSeconds(100));
        sub1.setStartTime(LocalDateTime.now());

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.NEW.name(), 1);
        sub2.setDuration(Duration.ofSeconds(100));
        sub2.setStartTime(LocalDateTime.now());

        manager.createSubTask(sub1);
        manager.createSubTask(sub2);
        assertArrayEquals(manager.getSubTasksValues().toArray(), manager.getEpicSubTasks(1).toArray());
    }

    @Test
    public void shouldReturnEmptyListWhenAllEpicsSubtasksDoNotExist() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );
        assertArrayEquals(new SubTask[]{}, manager.getEpicSubTasks(1).toArray());
    }

    @Test
    public void shouldReturnNullWhenEpicTaskIdIsNotCorrect() {
        assertNull(manager.getEpicSubTasks(10));
    }

    // epic status calculation
    @Test
    public void shouldBeStatusNewWhenAllSubtasksHaveNewStatus() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );

        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setStartTime(LocalDateTime.now());
        sub1.setDuration(Duration.ofSeconds(100));

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.NEW.name(), 1);
        sub2.setStartTime(LocalDateTime.now());
        sub2.setDuration(Duration.ofSeconds(100));

        manager.createSubTask(sub1);
        manager.createSubTask(sub2);
        assertEquals(Statuses.NEW.name(), manager.getEpicTaskById(1).getStatus());
    }

    @Test
    public void shouldBeStatusInProgressWhenSomeSubtasksHaveNewStatusAndSomeHaveDoneStatus() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );

        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setDuration(Duration.ofSeconds(100));
        sub1.setStartTime(LocalDateTime.now());

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.DONE.name(), 1);
        sub2.setDuration(Duration.ofSeconds(100));
        sub2.setStartTime(LocalDateTime.now());

        manager.createSubTask(sub1);
        manager.createSubTask(sub2);
        assertEquals(Statuses.IN_PROGRESS.name(), manager.getEpicTaskById(1).getStatus());
    }

    @Test
    public void shouldBeStatusDoneWhenAllSubtasksHaveDoneStatus() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );

        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.DONE.name(), 1);
        sub1.setStartTime(LocalDateTime.now());
        sub1.setDuration(Duration.ofSeconds(100));

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.DONE.name(), 1);
        sub2.setStartTime(LocalDateTime.now());
        sub2.setDuration(Duration.ofSeconds(100));

        manager.createSubTask(sub1);
        manager.createSubTask(sub2);
        assertEquals(Statuses.DONE.name(), manager.getEpicTaskById(1).getStatus());
    }

    // check epic in subtask
    @Test
    public void shouldCreateSubtaskWhenEpicIdInSubtaskIsCorrect() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );

        SubTask sub = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub.setDuration(Duration.ofSeconds(100));
        sub.setStartTime(LocalDateTime.now());

        manager.createSubTask(sub);
        assertEquals(1, manager.getSubTasksValues().size());
    }

    @Test
    public void shouldNotCreateSubtaskWhenEpicIdInSubtaskIsNotCorrect() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );
        manager.createSubTask(new SubTask("sub1", "sub1", Statuses.DONE.name(), 10));
        assertEquals(0, manager.getSubTasksValues().size());
    }
}
