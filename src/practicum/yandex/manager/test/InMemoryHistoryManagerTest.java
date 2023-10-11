package practicum.yandex.manager.test;

import org.junit.jupiter.api.BeforeEach;
import practicum.yandex.manager.InMemoryHistoryManager;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {
    @BeforeEach
    public void makeNewManager() {
        manager = new InMemoryHistoryManager();
    }
}