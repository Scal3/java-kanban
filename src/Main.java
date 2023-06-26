import practicum.yandex.manager.Manager;
import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        ArrayList<SubTask> subs = new ArrayList<>();
        EpicTask epic = new EpicTask("Epic", "Epic", "NEW", subs);
        SubTask sub = new SubTask("Sub", "Sub", "NEW", epic);
        subs.add(sub);

        manager.createEpicTask(epic);

        System.out.println(manager.getEpicSubTasks(2));
    }
}
