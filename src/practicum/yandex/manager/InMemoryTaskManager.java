package practicum.yandex.manager;

import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, EpicTask> epicTasks;
    protected final Map<Integer, SubTask> subTasks;
    protected final Set<Task> prioritizedTasks;
    protected final HistoryManager historyManager;
    protected int taskId;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epicTasks = new HashMap<>();
        subTasks = new HashMap<>();
        prioritizedTasks = new TreeSet<>((item1, item2) -> {
            if (item1.getStartTime() == null) return 1;

            if (item2.getStartTime() == null) return -1;

            if (item1.getStartTime().isAfter(item2.getStartTime())) {
                return 1;
            } else if(item2.getStartTime().isAfter(item1.getStartTime())) {
                return -1;
            } else {
                return 0;
            }
        });
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
        for (Task task : tasks.values()) {
            prioritizedTasks.remove(task);
        }

        tasks.clear();
    }

    @Override
    public void deleteAllEpicTasks() {
        epicTasks.clear();
        deleteAllSubTasks();
    }

    @Override
    public void deleteAllSubTasks() {
        for (SubTask sub : subTasks.values()) {
            prioritizedTasks.remove(sub);
        }

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
        if (task == null || !validateTaskByEndTime(task)) {
            return;
        }

        task.setId(++taskId);
        tasks.put(taskId, task);
        prioritizedTasks.add(task);
    }

    @Override
    public void createEpicTask(EpicTask task) {
        if (task == null) {
            return;
        }

        int epicId = ++taskId;
        task.setId(epicId);
        epicTasks.put(taskId, task);

        if (!task.getSubtasks().isEmpty()) {
            for (SubTask sub : task.getSubtasks()) {
                sub.setEpicId(epicId);
                createSubTask(sub);
            }
        }

        calculateEpicTaskData(task);
    }

    @Override
    public void createSubTask(SubTask task) {
        if (task == null || epicTasks.get(task.getEpicId()) == null || !validateTaskByEndTime(task)) {
            return;
        }

        task.setId(++taskId);
        subTasks.put(taskId, task);

        if (!epicTasks.get(task.getEpicId()).getSubtasks().contains(task)) {
            List<SubTask> epicsSubs = epicTasks.get(task.getEpicId()).getSubtasks();
            epicsSubs.add(task);
            epicTasks.get(task.getEpicId()).setSubtasks(epicsSubs);
        }

        calculateEpicTaskData(epicTasks.get(task.getEpicId()));
        prioritizedTasks.add(task);
    }

    @Override
    public void updateTask(Task task) {
        if (task == null || !tasks.containsKey(task.getId()) || !validateTaskByEndTime(task)) {
            return;
        }

        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
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

        calculateEpicTaskData(task);
        epicTasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask task) {
        if (task == null || !subTasks.containsKey(task.getId()) || !validateTaskByEndTime(task)) {
            return;
        }

        calculateEpicTaskData(getEpicTaskById(task.getEpicId()));
        subTasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            return;
        }

        Task task = tasks.get(id);

        tasks.remove(task.getId());
        removeFromTasksHistory(task.getId());
        prioritizedTasks.remove(task);
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

        SubTask sub = subTasks.get(id);
        EpicTask epic = getEpicTaskById(subTasks.get(id).getEpicId());

        epic.getSubtasks().remove(subTasks.get(id));
        subTasks.remove(id);
        removeFromTasksHistory(id);
        prioritizedTasks.remove(sub);
        calculateEpicTaskData(epic);
    }

    @Override
    public List<SubTask> getEpicSubTasks(int id) {
        return getEpicTaskById(id) != null ? new ArrayList<>(getEpicTaskById(id).getSubtasks()) : null;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    private void addToTasksHistory(Task task) {
        historyManager.add(task);
    }

    private void removeFromTasksHistory(int id) {
        historyManager.remove(id);
    }

    private void calculateEpicTaskData(EpicTask task) {
        if (task == null) return;

        calculateEpicTaskStatus(task);
        task.calculateTimes();
    }

    private void calculateEpicTaskStatus(EpicTask epic) {
        if (epic.getSubtasks() == null || epic.getSubtasks().isEmpty()) {
            epic.setStatus(Statuses.NEW.name());
        } else {
            String[] subTasksStatuses = new String[epic.getSubtasks().size()];

            for (int i = 0; i < epic.getSubtasks().size(); i++) {
                subTasksStatuses[i] = epic.getSubtasks().get(i).getStatus();
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
                epic.setStatus(Statuses.NEW.name());
            } else if (newCounter == 0 && doneCounter > 0) {
                epic.setStatus(Statuses.DONE.name());
            } else {
                epic.setStatus(Statuses.IN_PROGRESS.name());
            }
        }
    }

    private boolean validateTaskByEndTime(Task task) {
        if (task.getEndTime() == null) return true;

        for (Task taskFromPrioritizedTasks : prioritizedTasks) {
            if (
                    task.getEndTime().isAfter(taskFromPrioritizedTasks.getStartTime()) &&
                            taskFromPrioritizedTasks.getEndTime().isAfter(task.getStartTime())
            ) {
                return false;
            }
        }

        return true;
    }
}
