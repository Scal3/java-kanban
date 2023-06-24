import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

public class Main {

    public static void main(String[] args) {
        Task testTask = new Task("testTask", "testTask", "NEW");
        EpicTask testEpic = new EpicTask("testEpic", "testEpic" , "NEW", null);
        SubTask testSub = new SubTask("testSub", "testSub" , "NEW", null);

        System.out.println("testSub = " + testSub);
        System.out.println("testEpic = " + testEpic);
        System.out.println("testTask = " + testTask);
    }
}
