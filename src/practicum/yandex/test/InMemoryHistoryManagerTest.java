package practicum.yandex.test;

import org.junit.jupiter.api.BeforeEach;
import practicum.yandex.manager.InMemoryHistoryManager;
import practicum.yandex.test.HistoryManagerTest;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {
    @BeforeEach
    public void makeNewManager() {
        manager = new InMemoryHistoryManager();
    }
}