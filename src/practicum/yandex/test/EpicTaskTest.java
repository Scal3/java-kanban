package practicum.yandex.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practicum.yandex.manager.InMemoryTaskManager;
import practicum.yandex.manager.Statuses;
import practicum.yandex.manager.TaskManager;
import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
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
        assertEquals(Statuses.NEW.name(), manager.getEpicTaskById(1).getStatus());
    }

    @Test
    public void shouldBeStatusNewWithAllSubtasksThatHaveStatusNew() {
        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setStartTime(LocalDateTime.now());
        sub1.setDuration(Duration.ofSeconds(100));

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.NEW.name(), 1);
        sub2.setStartTime(LocalDateTime.now());
        sub2.setDuration(Duration.ofSeconds(100));

        manager.createEpicTask(
                new EpicTask(
                        "epic",
                        "epic",
                        Statuses.NEW.name(),
                        List.of(sub1, sub2)
                )
        );
        assertEquals(Statuses.NEW.name(), manager.getEpicTaskById(1).getStatus());
    }

    @Test
    public void shouldBeStatusDoneWithAllSubtasksThatHaveStatusDone() {
        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.DONE.name(), 1);
        sub1.setStartTime(LocalDateTime.now());
        sub1.setDuration(Duration.ofSeconds(100));

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.DONE.name(), 1);
        sub2.setStartTime(LocalDateTime.now());
        sub2.setDuration(Duration.ofSeconds(100));

        manager.createEpicTask(
                new EpicTask(
                        "epic",
                        "epic",
                        Statuses.NEW.name(),
                        List.of(sub1, sub2)
                )
        );
        assertEquals(Statuses.DONE.name(), manager.getEpicTaskById(1).getStatus());
    }

    @Test
    public void shouldBeStatusInProgressWithAllSubtasksThatHaveStatusesNewAndDone() {
        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setStartTime(LocalDateTime.now());
        sub1.setDuration(Duration.ofSeconds(100));

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.DONE.name(), 1);
        sub2.setStartTime(LocalDateTime.now());
        sub2.setDuration(Duration.ofSeconds(100));

        manager.createEpicTask(
                new EpicTask(
                        "epic",
                        "epic",
                        Statuses.NEW.name(),
                        List.of(sub1, sub2)
                )
        );
        assertEquals(Statuses.IN_PROGRESS.name(), manager.getEpicTaskById(1).getStatus());
    }

    @Test
    public void shouldBeStatusDoneWithAllSubtasksThatHaveStatusInProgress() {
        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.IN_PROGRESS.name(), 1);
        sub1.setStartTime(LocalDateTime.now());
        sub1.setDuration(Duration.ofSeconds(100));

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.IN_PROGRESS.name(), 1);
        sub2.setStartTime(LocalDateTime.now());
        sub2.setDuration(Duration.ofSeconds(100));

        manager.createEpicTask(
                new EpicTask(
                        "epic",
                        "epic",
                        Statuses.NEW.name(),
                        List.of(sub1, sub2)
                )
        );
        assertEquals(Statuses.DONE.name(), manager.getEpicTaskById(1).getStatus());
    }

    @Test
    public void shouldCalculateStartTimeDurationAndEndTime() {
        manager.createEpicTask(
                new EpicTask("epic", "epic", Statuses.NEW.name(), Collections.emptyList())
        );

        LocalDateTime expectedStartTime = LocalDateTime.of(2023, Month.JULY, 20, 10, 30);
        LocalDateTime lateStartTime = LocalDateTime.now();
        Duration duration = Duration.ofSeconds(100);
        Duration expectedDuration = duration.plus(duration);
        LocalDateTime expectedEndTime = lateStartTime.plus(duration);

        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setStartTime(expectedStartTime);
        sub1.setDuration(duration);

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.NEW.name(), 1);
        sub2.setStartTime(lateStartTime);
        sub2.setDuration(duration);

        manager.createSubTask(sub1);
        manager.createSubTask(sub2);

        assertEquals(expectedStartTime, manager.getEpicTaskById(1).getStartTime());
        assertEquals(expectedDuration, manager.getEpicTaskById(1).getDuration());
        assertEquals(expectedEndTime, manager.getEpicTaskById(1).getEndTime());
    }
}