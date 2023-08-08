import practicum.yandex.manager.InMemoryTaskManager;
import practicum.yandex.manager.TaskManager;
import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();
        List<SubTask> subs = new ArrayList<>();
        List<SubTask> subs2 = new ArrayList<>();

        EpicTask epic1 = new EpicTask("epic", "epic", "NEW", subs);
        EpicTask epic2 = new EpicTask("epic2", "epic2", "NEW", subs2);
        SubTask sub1 = new SubTask("sub1", "sub1", "DONE", epic1);
        SubTask sub2 = new SubTask("sub2", "sub2", "DONE", epic1);
        SubTask sub3 = new SubTask("sub3", "sub3", "DONE", epic1);

        subs.add(sub1);
        subs.add(sub2);
        subs.add(sub3);

        manager.createEpicTask(epic1);
        manager.createEpicTask(epic2);
    }
}
