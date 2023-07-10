import practicum.yandex.manager.InMemoryTaskManager;
import practicum.yandex.manager.Manager;
import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Manager manager = new InMemoryTaskManager();
        List<SubTask> subs = new ArrayList<>();
        EpicTask epic = new EpicTask("epic", "epic", "NEW", subs);
        SubTask sub1 = new SubTask("sub1", "sub1", "DONE", epic);
        SubTask sub2 = new SubTask("sub2", "sub2", "DONE", epic);

        subs.add(sub1);
        subs.add(sub2);
        manager.createEpicTask(epic);
        System.out.println(manager.getEpicTaskById(3));
    }
}
