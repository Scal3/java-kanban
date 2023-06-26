package practicum.yandex.manager;

import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    private final String STATUS_NEW = "NEW";
    private final String STATUS_DONE = "DONE";
    private final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    private final Map<Integer, Task> tasks;
    private final Map<Integer, EpicTask> epicTasks;
    private final Map<Integer, SubTask> subTasks;
    private int taskId;

    public Manager() {
        tasks = new HashMap<>();
        epicTasks = new HashMap<>();
        subTasks = new HashMap<>();
        taskId = 0;
    }

    public Collection<Task> getTasksValues() {
        return new ArrayList<>(tasks.values());
    }

    public Collection<EpicTask> getEpicTasksValues() {
        return new ArrayList<>(epicTasks.values());
    }

    public Collection<SubTask> getSubTasksValues() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpicTasks() {
        epicTasks.clear();
        deleteAllSubTasks();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();

        for (EpicTask epic : epicTasks.values()) {
            epic.setSubtasks(null);
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public EpicTask getEpicTaskById(int id) {
        return epicTasks.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public void createTask(Task task) {
        if (task != null) {
            task.setId(++taskId);
            tasks.put(taskId, task);
        }
    }

    public void createEpicTask(EpicTask task) {
        if (task != null) {
            if (!task.getSubtasks().isEmpty()) {
                for (SubTask sub : task.getSubtasks()) {
                    createSubTask(sub);
                }
            }

            calculateEpicTaskStatus(task);
            task.setId(++taskId);
            epicTasks.put(taskId, task);
        }
    }

    public void createSubTask(SubTask task) {
        if (task != null) {
            task.setId(++taskId);
            subTasks.put(taskId, task);
            EpicTask epic = task.getEpicTaskReference();
            calculateEpicTaskStatus(epic);
        }
    }

    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpicTask(EpicTask task) {
        if (task != null && epicTasks.containsKey(task.getId())) {
            EpicTask oldEpic = epicTasks.get(task.getId());

            for (Integer key : subTasks.keySet()) {
                SubTask subTask = subTasks.get(key);
                EpicTask epicFromSub = subTask.getEpicTaskReference();

                if (oldEpic.equals(epicFromSub)) {
                    subTasks.remove(key);
                }
            }

            if (task.getSubtasks() != null && !task.getSubtasks().isEmpty()) {
                for (SubTask sub : task.getSubtasks()) {
                    createSubTask(sub);
                }
            }

            calculateEpicTaskStatus(task);
            epicTasks.put(task.getId(), task);
        }
    }

    public void updateSubTask(SubTask task) {
        if (task != null && tasks.containsKey(task.getId())) {
            EpicTask epicFromUnUpdatedSub = subTasks.get(task.getId()).getEpicTaskReference();
            EpicTask epicFromUpdatedSub = task.getEpicTaskReference();

            if (epicFromUnUpdatedSub.equals(epicFromUpdatedSub)) {
                calculateEpicTaskStatus(epicFromUpdatedSub);
            }

            subTasks.put(task.getId(), task);
        }
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicTaskById(int id) {
        if (epicTasks.containsKey(id)) {
            EpicTask epic = epicTasks.get(id);

            if (!epic.getSubtasks().isEmpty()) {
                for (Integer key : subTasks.keySet()) {
                    SubTask subFromSubTasks = subTasks.get(key);

                    if (subFromSubTasks != null && subFromSubTasks.getEpicTaskReference().equals(epic)) {
                        subTasks.remove(key);
                    }
                }
            }

            epicTasks.remove(id);
        }
    }

    public void deleteSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            SubTask sub = subTasks.get(id);
            EpicTask epic = subTasks.get(id).getEpicTaskReference();

            epic.getSubtasks().remove(sub);
            subTasks.remove(id);
            calculateEpicTaskStatus(epic);
        }
    }

    public SubTask[] getEpicSubTasks(int id) {
        EpicTask epic = getEpicTaskById(id);

        if (epic != null) {
            SubTask[] arr = new SubTask[epic.getSubtasks().size()];

            for (int i = 0; i < epic.getSubtasks().size(); i++) {
                arr[i] = epic.getSubtasks().get(i);
            }

            return arr;
        } else {
            return null;
        }
    }

    private void calculateEpicTaskStatus(EpicTask task) {
        if (task.getSubtasks() == null || task.getSubtasks().isEmpty()) {
            task.setStatus(STATUS_NEW);
        } else {
            String[] subTasksStatuses = new String[task.getSubtasks().size()];

            for (int i = 0; i < task.getSubtasks().size(); i++) {
                subTasksStatuses[i] = task.getSubtasks().get(i).getStatus();
            }

            int doneCounter = 0;
            int newCounter = 0;

            for (String status : subTasksStatuses) {
                if (status.equals(STATUS_NEW)) {
                    newCounter++;
                } else {
                    doneCounter++;
                }
            }

            if (doneCounter == 0 && newCounter > 0) {
                task.setStatus(STATUS_NEW);
            } else if (newCounter == 0 && doneCounter > 0) {
                task.setStatus(STATUS_DONE);
            } else {
                task.setStatus(STATUS_IN_PROGRESS);
            }
        }
    }
}
