package practicum.yandex.manager.test;

import org.junit.jupiter.api.BeforeEach;
import practicum.yandex.manager.InMemoryTaskManager;
import practicum.yandex.manager.test.TaskManagerTest;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void makeNewManager() {
        manager = new InMemoryTaskManager();
    }
}