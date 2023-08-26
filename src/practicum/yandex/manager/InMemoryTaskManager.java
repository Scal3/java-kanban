package practicum.yandex.manager;

import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, EpicTask> epicTasks;
    private final Map<Integer, SubTask> subTasks;
    private final HistoryManager historyManager;
    private int taskId;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epicTasks = new HashMap<>();
        subTasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        taskId = 0;
    }

    @Override
    public List<Task> getTasksValues() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<EpicTask> getEpicTasksValues() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public List<SubTask> getSubTasksValues() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpicTasks() {
        epicTasks.clear();
        deleteAllSubTasks();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();

        for (EpicTask epic : epicTasks.values()) {
            epic.setSubtasks(null);
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (!tasks.containsKey(id)) {
            return null;
        }

        addToTasksHistory(tasks.get(id));

        return tasks.get(id);
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        if (!epicTasks.containsKey(id)) {
            return null;
        }

        addToTasksHistory(epicTasks.get(id));

        return epicTasks.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            return null;
        }

        addToTasksHistory(subTasks.get(id));

        return subTasks.get(id);
    }

    @Override
    public void createTask(Task task) {
        if (task == null) {
            return;
        }

        task.setId(++taskId);
        tasks.put(taskId, task);
    }

    @Override
    public void createEpicTask(EpicTask task) {
        if (task == null) {
            return;
        }

        if (!task.getSubtasks().isEmpty()) {
            for (SubTask sub : task.getSubtasks()) {
                createSubTask(sub);
            }
        }

        calculateEpicTaskStatus(task);
        task.setId(++taskId);
        epicTasks.put(taskId, task);
    }

    @Override
    public void createSubTask(SubTask task) {
        if (task == null) {
            return;
        }

        task.setId(++taskId);
        subTasks.put(taskId, task);
        calculateEpicTaskStatus(task.getEpicTaskReference());
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpicTask(EpicTask task) {
        if (task == null || !epicTasks.containsKey(task.getId())) {
            return;
        }

        for (SubTask sub : epicTasks.get(task.getId()).getSubtasks()) {
            subTasks.remove(sub.getId());
        }

        if (task.getSubtasks() != null && !task.getSubtasks().isEmpty()) {
            for (SubTask sub : task.getSubtasks()) {
                createSubTask(sub);
            }
        }

        calculateEpicTaskStatus(task);
        epicTasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask task) {
        if (task == null || !tasks.containsKey(task.getId())) {
            return;
        }

        calculateEpicTaskStatus(task.getEpicTaskReference());
        subTasks.put(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        removeFromTasksHistory(id);
    }

    @Override
    public void deleteEpicTaskById(int id) {
        EpicTask epic = epicTasks.get(id);

        if (epic == null) {
            return;
        }

        if (epic.getSubtasks() != null && !epic.getSubtasks().isEmpty()) {
            for (SubTask sub : epic.getSubtasks()) {
                subTasks.remove(sub.getId());
                removeFromTasksHistory(sub.getId());
            }
        }

        epicTasks.remove(id);
        removeFromTasksHistory(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            return;
        }

        EpicTask epic = subTasks.get(id).getEpicTaskReference();

        epic.getSubtasks().remove(subTasks.get(id));
        subTasks.remove(id);
        removeFromTasksHistory(id);
        calculateEpicTaskStatus(epic);
    }

    @Override
    public List<SubTask> getEpicSubTasks(int id) {
        return getEpicTaskById(id) != null ? new ArrayList<>(getEpicTaskById(id).getSubtasks()) : null;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected void addToTasksHistory(Task task) {
        historyManager.add(task);
    }

    protected void removeFromTasksHistory(int id) {
        historyManager.remove(id);
    }

    private void calculateEpicTaskStatus(EpicTask task) {
        if (task.getSubtasks() == null || task.getSubtasks().isEmpty()) {
            task.setStatus(Statuses.NEW.name());
        } else {
            String[] subTasksStatuses = new String[task.getSubtasks().size()];

            for (int i = 0; i < task.getSubtasks().size(); i++) {
                subTasksStatuses[i] = task.getSubtasks().get(i).getStatus();
            }

            int doneCounter = 0;
            int newCounter = 0;

            for (String status : subTasksStatuses) {
                if (status.equals(Statuses.NEW.name())) {
                    newCounter++;
                } else {
                    doneCounter++;
                }
            }

            if (doneCounter == 0 && newCounter > 0) {
                task.setStatus(Statuses.NEW.name());
            } else if (newCounter == 0 && doneCounter > 0) {
                task.setStatus(Statuses.DONE.name());
            } else {
                task.setStatus(Statuses.IN_PROGRESS.name());
            }
        }
    }
}
