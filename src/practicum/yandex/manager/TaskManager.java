package practicum.yandex.manager;

import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getPrioritizedTasks();
    List<Task> getHistory();
    List<Task> getTasksValues();
    List<EpicTask> getEpicTasksValues();
    List<SubTask> getSubTasksValues();
    void deleteAllTasks();
    void deleteAllEpicTasks();
    void deleteAllSubTasks();
    Task getTaskById(int id);
    EpicTask getEpicTaskById(int id);
    SubTask getSubTaskById(int id);
    void createTask(Task task);
    void createEpicTask(EpicTask task);
    void createSubTask(SubTask task);
    void updateTask(Task task);
    void updateEpicTask(EpicTask task);
    void updateSubTask(SubTask task);
    void deleteTaskById(int id);
    void deleteEpicTaskById(int id);
    void deleteSubTaskById(int id);
    List<SubTask> getEpicSubTasks(int id);
}
