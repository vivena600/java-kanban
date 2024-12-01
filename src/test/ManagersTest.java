package test;

import controlles.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    static Managers managers;
    static HistoryManager memoryManager;
    static TaskManager taskManager;

    @BeforeAll
    static void setManagers() {
        managers = new Managers();
    }

    @Test
    void getDefault() {
        taskManager = managers.getDefault();
        assertNotNull(taskManager, "Возвращают null");
        assertTrue(taskManager instanceof InMemoryTaskManager, "Создается экземпляр другого типа");

    }

    @Test
    void getDefaultHistory() {
        memoryManager = managers.getDefaultHistory();
        assertNotNull(memoryManager, "Возвращают null");
        assertTrue(memoryManager instanceof InMemoryHistoryManager, "Создается экземпляр другого класса");

    }
}