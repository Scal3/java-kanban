package practicum.yandex.manager;

import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static final boolean SUCCESS = true;
    private static final boolean FALSE = false;
    private static final byte MAX_HISTORY_QUANTITY = 10;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, EpicTask> epicTasks;
    private final Map<Integer, SubTask> subTasks;
    private final Map<Integer, Task> tasksHistory;
    private int historyTasksCounter;
    private int taskId;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epicTasks = new HashMap<>();
        subTasks = new HashMap<>();
        tasksHistory = new HashMap<>();
        historyTasksCounter = 0;
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
    public boolean deleteAllTasks() {
        tasks.clear();
        return SUCCESS;
    }

    @Override
    public boolean deleteAllEpicTasks() {
        epicTasks.clear();
        deleteAllSubTasks();

        return SUCCESS;
    }

    @Override
    public boolean deleteAllSubTasks() {
        subTasks.clear();

        for (EpicTask epic : epicTasks.values()) {
            epic.setSubtasks(null);
        }

        return SUCCESS;
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            addToTasksHistory(tasks.get(id));
            return tasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        if (epicTasks.containsKey(id)) {
            addToTasksHistory(epicTasks.get(id));
            return epicTasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            addToTasksHistory(subTasks.get(id));
            return subTasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public boolean createTask(Task task) {
        if (task == null) {
            return FALSE;
        }

        task.setId(++taskId);
        tasks.put(taskId, task);

        return SUCCESS;
    }

    @Override
    public boolean createEpicTask(EpicTask task) {
        if (task == null) {
            return FALSE;
        }

        if (!task.getSubtasks().isEmpty()) {
            for (SubTask sub : task.getSubtasks()) {
                createSubTask(sub);
            }
        }

        calculateEpicTaskStatus(task);
        task.setId(++taskId);
        epicTasks.put(taskId, task);

        return SUCCESS;
    }

    @Override
    public boolean createSubTask(SubTask task) {
        if (task == null) {
            return FALSE;
        }

        task.setId(++taskId);
        subTasks.put(taskId, task);
        calculateEpicTaskStatus(task.getEpicTaskReference());

        return SUCCESS;
    }

    @Override
    public boolean updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }

        return SUCCESS;
    }

    @Override
    public boolean updateEpicTask(EpicTask task) {
        if (task == null || !epicTasks.containsKey(task.getId())) {
            return FALSE;
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

        return SUCCESS;
    }

    @Override
    public boolean updateSubTask(SubTask task) {
        if (task == null || !tasks.containsKey(task.getId())) {
            return FALSE;
        }

        calculateEpicTaskStatus(task.getEpicTaskReference());
        subTasks.put(task.getId(), task);

        return SUCCESS;
    }

    @Override
    public boolean deleteTaskById(int id) {
        tasks.remove(id);
        return SUCCESS;
    }

    @Override
    public boolean deleteEpicTaskById(int id) {
        if (epicTasks.get(id) != null && !epicTasks.get(id).getSubtasks().isEmpty()) {
            for (SubTask sub : epicTasks.get(id).getSubtasks()) {
                subTasks.remove(sub.getId());
            }
        }

        epicTasks.remove(id);
        return SUCCESS;
    }

    @Override
    public boolean deleteSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            return FALSE;
        }

        EpicTask epic = subTasks.get(id).getEpicTaskReference();

        epic.getSubtasks().remove(subTasks.get(id));
        subTasks.remove(id);
        calculateEpicTaskStatus(epic);

        return SUCCESS;
    }

    @Override
    public List<SubTask> getEpicSubTasks(int id) {
        return getEpicTaskById(id) != null ? new ArrayList<>(getEpicTaskById(id).getSubtasks()) : null;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();

        if (historyTasksCounter <= MAX_HISTORY_QUANTITY) {
            for (int i = historyTasksCounter; i > 0; i--) {
                history.add(tasksHistory.get(i));
            }
        } else {
            for (int i = historyTasksCounter; i > historyTasksCounter - MAX_HISTORY_QUANTITY; i--) {
                history.add(tasksHistory.get(i));
            }
        }

        return history;
    }

    private void addToTasksHistory(Task task) {
        tasksHistory.put(++historyTasksCounter, task);
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
