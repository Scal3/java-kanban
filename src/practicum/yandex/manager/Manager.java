package practicum.yandex.manager;

import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

import java.util.Collection;
import java.util.List;

public interface Manager {
    List<Task> getHistory();
    List<Task> getTasksValues();
    List<EpicTask> getEpicTasksValues();
    List<SubTask> getSubTasksValues();
    boolean deleteAllTasks();
    boolean deleteAllEpicTasks();
    boolean deleteAllSubTasks();
    Task getTaskById(int id);
    EpicTask getEpicTaskById(int id);
    SubTask getSubTaskById(int id);
    boolean createTask(Task task);
    boolean createEpicTask(EpicTask task);
    boolean createSubTask(SubTask task);
    boolean updateTask(Task task);
    boolean updateEpicTask(EpicTask task);
    boolean updateSubTask(SubTask task);
    boolean deleteTaskById(int id);
    boolean deleteEpicTaskById(int id);
    boolean deleteSubTaskById(int id);
    List<SubTask> getEpicSubTasks(int id);
}
