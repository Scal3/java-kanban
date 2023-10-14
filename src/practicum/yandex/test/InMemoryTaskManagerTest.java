package practicum.yandex.test;

import org.junit.jupiter.api.BeforeEach;
import practicum.yandex.manager.InMemoryTaskManager;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    @Override
    public void makeManager() {
        manager = new InMemoryTaskManager();
    }
}