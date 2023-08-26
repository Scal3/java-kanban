import practicum.yandex.manager.FileBackedTasksManager;
import practicum.yandex.manager.InMemoryTaskManager;
import practicum.yandex.manager.TaskManager;
import practicum.yandex.manager.TaskTypes;
import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File file = new File("C:/WEB/java-practicum/projects/kanban/src/test.csv");
        FileBackedTasksManager.loadFromFile(file);

//        TaskManager manager = new FileBackedTasksManager(file);
//        List<SubTask> subs = new ArrayList<>();
//        List<SubTask> subs2 = new ArrayList<>();
//
//        Task task1 = new Task("task1", "task1", "NEW");
//        Task task2 = new Task("task2", "task2", "NEW");
//        Task task3 = new Task("task3", "task3", "NEW");
//
//        EpicTask epic1 = new EpicTask("epic", "epic", "NEW", subs);
//        EpicTask epic2 = new EpicTask("epic2", "epic2", "NEW", subs2);
//        SubTask sub1 = new SubTask("sub1", "sub1", "DONE", epic1);
//        SubTask sub2 = new SubTask("sub2", "sub2", "DONE", epic1);
//        SubTask sub3 = new SubTask("sub3", "sub3", "DONE", epic1);
//
//        subs.add(sub1);
//        subs.add(sub2);
//        subs.add(sub3);
//
//        manager.createEpicTask(epic1);
//        manager.createEpicTask(epic2);
//
//        manager.createTask(task1);
//        manager.createTask(task2);
//        manager.createTask(task3);
//
//        manager.getTaskById(6);
//        manager.getTaskById(7);
//        manager.getTaskById(8);
    }
}
