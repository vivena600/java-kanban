public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Дописать код", "для 4-го спринта");
        taskManager.add(task1);
        Epic epic1 = new Epic("Дописать курсовую", "Иначе у тебя незачёт) ");
        taskManager.add(epic1);
        SubTask subTask1 = new SubTask("Прочитать теорию", "выбрать метод хеширования", epic1.id);
        taskManager.add(subTask1);
        SubTask subTask2 = new SubTask("Написать код", "", epic1.id);
        taskManager.add(subTask2);
        Epic epic2 = new Epic("эпик2", "описание");
        taskManager.add(epic2);
        SubTask subTask3 = new SubTask("задача1", "описание", epic2.id);
        taskManager.add(subTask3);

        taskManager.outputTask();
        System.out.println("-----------------------------------------------------------------------------------------");

        Task task1Update = new Task(task1.title, task1.description, TaskStatus.IN_PROGRESS, task1.id);
        taskManager.update(task1Update);

        SubTask subTaskUpdate = new SubTask(subTask3.title, subTask3.description, TaskStatus.DONE, subTask3.id,
                subTask3.epicId);
        taskManager.update(subTaskUpdate);
        taskManager.update(epic2);

        taskManager.outputTask();
        System.out.println("-----------------------------------------------------------------------------------------");

        taskManager.removeTaskById(5);
        taskManager.outputTask();

        System.out.println("-----------------------------------------------------------------------------------------");

        taskManager.getTaskById(1);

        System.out.println("-----------------------------------------------------------------------------------------");
        taskManager.clearAllTask();
        taskManager.outputTask();
    }
}