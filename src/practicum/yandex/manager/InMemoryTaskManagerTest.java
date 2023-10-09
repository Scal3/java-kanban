package practicum.yandex.manager;

import org.junit.jupiter.api.BeforeEach;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void makeNewManager() {
        manager = new InMemoryTaskManager();
    }
}