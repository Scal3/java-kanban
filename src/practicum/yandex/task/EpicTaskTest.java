package practicum.yandex.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practicum.yandex.manager.InMemoryTaskManager;
import practicum.yandex.manager.Statuses;
import practicum.yandex.manager.TaskManager;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {
    TaskManager manager = new InMemoryTaskManager();

    @BeforeEach
    public void makeNewManager() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void shouldBeStatusNewWithEmptySubtasksList() {
        manager.createEpicTask(
            new EpicTask(
                "epic",
                "epic",
                Statuses.NEW.name(),
                Collections.emptyList()
            )
        );
        assertEquals(Statuses.NEW.name(), manager.getEpicTaskById(1).status);
    }

    @Test
    public void shouldBeStatusNewWithAllSubtasksThatHaveStatusNew() {
        manager.createEpicTask(
                new EpicTask(
                        "epic",
                        "epic",
                        Statuses.NEW.name(),
                        List.of(
                                new SubTask("sub1", "sub1", Statuses.NEW.name(), 1),
                                new SubTask("sub2", "sub2", Statuses.NEW.name(), 1)
                        )
                )
        );
        assertEquals(Statuses.NEW.name(), manager.getEpicTaskById(1).status);
    }

    @Test
    public void shouldBeStatusDoneWithAllSubtasksThatHaveStatusDone() {
        manager.createEpicTask(
                new EpicTask(
                        "epic",
                        "epic",
                        Statuses.NEW.name(),
                        List.of(
                                new SubTask("sub1", "sub1", Statuses.DONE.name(), 1),
                                new SubTask("sub2", "sub2", Statuses.DONE.name(), 1)
                        )
                )
        );
        assertEquals(Statuses.DONE.name(), manager.getEpicTaskById(1).status);
    }

    @Test
    public void shouldBeStatusInProgressWithAllSubtasksThatHaveStatusesNewAndDone() {
        manager.createEpicTask(
                new EpicTask(
                        "epic",
                        "epic",
                        Statuses.NEW.name(),
                        List.of(
                                new SubTask("sub1", "sub1", Statuses.NEW.name(), 1),
                                new SubTask("sub2", "sub2", Statuses.DONE.name(), 1)
                        )
                )
        );
        assertEquals(Statuses.IN_PROGRESS.name(), manager.getEpicTaskById(1).status);
    }

    @Test
    public void shouldBeStatusDoneWithAllSubtasksThatHaveStatusInProgress() {
        manager.createEpicTask(
                new EpicTask(
                        "epic",
                        "epic",
                        Statuses.NEW.name(),
                        List.of(
                                new SubTask("sub1", "sub1", Statuses.IN_PROGRESS.name(), 1),
                                new SubTask("sub2", "sub2", Statuses.IN_PROGRESS.name(), 1)
                        )
                )
        );
        assertEquals(Statuses.DONE.name(), manager.getEpicTaskById(1).status);
    }
}