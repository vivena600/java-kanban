import controlles.InMemoryHistoryManager;
import controlles.Managers;
import controlles.TaskManager;
import model.*;
import controlles.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        /*
        task = new Task("Test addNewTask", "Test addNewTask description", Duration.ofMinutes(30), LocalDateTime.of(2025, 3, 5, 15, 15));
        epic = new Epic("Test addNewSubStac epic", "Test addNewSubTask epic description", 2, null, null);
        subTask = new SubTask("Test addNewSubTask", "Test add NewSubtask descriprion", 2, Duration.ofMinutes(12), LocalDateTime.of(2025, 3, 5, 14, 15));
        subTask2 = new SubTask("Test addNewSubTask", "Test add NewSubtask descriprion", TaskStatus.IN_PROGRESS, 2, 2, Duration.ofMinutes(12), LocalDateTime.of(2025, 3, 5, 14, 10));

        taskManager.add(task);
        taskManager.add(epic);
        taskManager.add(subTask);
        taskManager.add(subTask2);

         */
    }
}