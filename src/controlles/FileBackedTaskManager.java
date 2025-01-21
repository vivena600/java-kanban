package controlles;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private String fileName;

    public FileBackedTaskManager(String fileName) {
        this.fileName = fileName;
    }

    private TypeTask getType(Task task) {
        if (task instanceof SubTask) {
            return TypeTask.SUBTASK;
        } else if (task instanceof Epic) {
            return TypeTask.EPIC;
        } else {
            return TypeTask.TASK;
        }
    }

    private int getEpicId(Task task) {
        return ((SubTask) task).getEpicId();
    }

    public String toString(Task task) {
        List<String> line = new ArrayList<>(List.of(String.valueOf(task.getId()), String.valueOf(getType(task)),
                task.getTitle(), String.valueOf(task.getStatus()), task.getDescription()));
        if (getType(task) == TypeTask.SUBTASK) {
            line.add(String.valueOf(getEpicId(task)));
        }
        return String.join(",", line);
    }

    public static Task fromString(String line) {
        String[] params = line.split(",");
        int id = Integer.parseInt(params[0]);
        TypeTask type = TypeTask.valueOf(params[1]);
        String title = params[2];
        TaskStatus status = TaskStatus.valueOf(params[3]);
        String description = params[4];

        if (type == TypeTask.TASK) {
            Task task = new Task(title, description, status, id);
            return task;
        } else if (type == TypeTask.EPIC) {
            Epic epic = new Epic(title, description, status, id);
            return epic;
        } else {
            int epicId = Integer.parseInt(params[5]);
            SubTask subTask = new SubTask(title, description, status, id, epicId);
            return subTask;
        }
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                bufferedWriter.write(toString(task) + "\n");
            }

            for (Task epic : getEpics()) {
                bufferedWriter.write(toString(epic) + "\n");
            }

            for (Task subtask : getSubTasks()) {
                bufferedWriter.write(toString(subtask) + "\n");
            }
        } catch (IOException ex) {
            throw new ManagerSaveException("Ошибка при записи в файл");
        }
    }

    static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager newFileBackedTaskManager = new FileBackedTaskManager(file.getAbsolutePath());
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = bufferedReader.readLine(); // пропускаем первую строчку файла, которая является заголовком
            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();
                Task task = fromString(line);
                newFileBackedTaskManager.add(task);
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Не удалось найти указанный файл");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла");
        }
        return newFileBackedTaskManager;
    }

    public static void main(String[] args) {
        File file = new File("test.csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.getName());
        Task task1 = new Task("Задача 1", "Описание 1"); //id = 1
        Task task2 = new Task("Задача 2", "Описание 2"); //id = 2
        fileBackedTaskManager.add(task1);
        fileBackedTaskManager.add(task2);
        Epic epic1 = new Epic("Эпик 1", "эпик с 3 подзадачами"); //id = 3
        Epic epic2 = new Epic("Эпик 2", "эпик без задач"); //id = 4
        fileBackedTaskManager.add(epic1);
        fileBackedTaskManager.add(epic2);
        SubTask subTask1 = new SubTask("подзадача 1", "описание 1", epic1.getId()); //id = 5
        SubTask subTask2 = new SubTask("подзадача 2", "описание 2", epic1.getId()); //id = 6
        SubTask subTask3 = new SubTask("подзадача 3", "описание 3", epic1.getId()); //id = 7
        fileBackedTaskManager.add(subTask1);
        fileBackedTaskManager.add(subTask2);
        fileBackedTaskManager.add(subTask3);
        Task task2Update = new Task("Задача 2", "Описание 2", TaskStatus.IN_PROGRESS, 2);
        fileBackedTaskManager.update(task2Update);
        FileBackedTaskManager newFileBacked = loadFromFile(file);
        Task task3 = new Task("Задача 3", "Описание 3"); //id = 8
        Task task1Update = new Task("Задача 1", "Описание 1", TaskStatus.DONE, 1);
        newFileBacked.update(task1Update);
        newFileBacked.add(task3);
    }

    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void add(SubTask subTask) {
        super.add(subTask);
        save();
    }

    @Override
    public void update(SubTask subTask) {
        super.update(subTask);
        save();
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void updateEpicStatuc(Epic epic) {
        super.updateEpicStatuc(epic);
        save();
    }

    @Override
    public void clearAllTask() {
        super.clearAllTask();
        save();
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpic() {
        super.deleteEpic();
        save();
    }

    @Override
    public void deleteTasks(int id) {
        super.deleteTasks(id);
        save();
    }

    @Override
    public void deleteSubtasks(int id) {
        super.deleteSubtasks(id);
        save();
    }

    @Override
    public void deleteEpics(int id) {
        super.deleteEpics(id);
        save();
    }
}
