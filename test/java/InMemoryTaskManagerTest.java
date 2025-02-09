import model.*;
import controlles.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    static InMemoryTaskManager taskManager;
    static Task task;
    static Epic epic;
    static SubTask subTask;
    static SubTask subTask2;

    @Override
    @BeforeEach
    void input() {
        manager = new InMemoryTaskManager();
    }

    @Override
    @Test
    void updateEndTime() {
        Epic epic = new Epic("title3", "discription3", null, null);
        manager.add(epic);
        SubTask subTask1 = new SubTask("title3", "discription3", epic.getId(), Duration.ofMinutes(3),
                LocalDateTime.of(2025, 3, 3, 13, 15));
        SubTask subTask2 = new SubTask("title4", "discription4", epic.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 6, 15, 15));
        manager.add(subTask1);
        manager.add(subTask2);

        assertEquals(epic.getStartTime(),  LocalDateTime.of(2025, 3, 3, 13, 15),
                "Эпик неправильно записывает начальное время");
        assertEquals(epic.getDuration(), Duration.ofMinutes(15), "Не коррректное время выполнения эпика");
        assertEquals(epic.getEndTime(), subTask1.getEndTime().plus(subTask2.getDuration()),
                "Не коррректное время выполнения эпика");
        assertEquals(epic.getSubTaskId().size(), 2, "В Эпике не корректное число подзадач");
    }
}