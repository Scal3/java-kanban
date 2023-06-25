package practicum.yandex.control;

import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Manager {
    private final String STATUS_NEW = "NEW";
    private final String STATUS_DONE = "DONE";
    private final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, EpicTask> epicTasks;
    private HashMap<Integer, SubTask> subTasks;
    private int taskId;

    public Manager() {
        tasks = new HashMap<>();
        epicTasks = new HashMap<>();
        subTasks = new HashMap<>();
        taskId = 0;
    }

    public Collection<Task> getTasksValues() {
        return tasks.values();
    }

    public Collection<EpicTask> getEpicTasksValues() {
        return epicTasks.values();
    }

    public Collection<SubTask> getSubTasksValues() {
        return subTasks.values();
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
            if (epic != null) {
                epic.setSubtasks(null);
            }
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
            tasks.put(++taskId, task);
        }
    }

    public void createEpicTask(EpicTask task) {
        if (task != null) {
            if (!task.getSubtasks().isEmpty()) {
                for (SubTask sub : task.getSubtasks()) {
                    createSubTask(sub);
                }
                calculateEpicTaskStatus(task);
            } else {
                calculateEpicTaskStatus(task);
            }

            epicTasks.put(++taskId, task);
        }
    }

    public void createSubTask(SubTask task) {
        if (task != null) {
            subTasks.put(++taskId, task);
            EpicTask epic = task.getEpicTaskReference();
            calculateEpicTaskStatus(epic);
        }
    }

    public void updateTaskById(int id, Task task) {
        if (tasks.containsKey(id) && task != null) {
            tasks.put(id, task);
        }
    }

    public void updateEpicTaskById(int id, EpicTask task) {
        if (epicTasks.containsKey(id) && task != null) {
            EpicTask oldEpic = epicTasks.get(id);

            for (int i = 1; i <= taskId; i++) {
                SubTask subTask = subTasks.get(i);

                if (subTask != null) {
                    EpicTask epicFromSub = subTask.getEpicTaskReference();

                    if (oldEpic.equals(epicFromSub)) {
                        subTasks.remove(i);
                    }
                }
            }

            if (task.getSubtasks() != null && !task.getSubtasks().isEmpty()) {
                for (SubTask sub : task.getSubtasks()) {
                    createSubTask(sub);
                }

                calculateEpicTaskStatus(task);
            } else {
                calculateEpicTaskStatus(task);
            }

            epicTasks.put(id, task);
        }
    }

    public void updateSubTaskById(int id, SubTask task) {
        if (subTasks.containsKey(id) && task != null) {
            EpicTask epicFromUnUpdatedSub = subTasks.get(id).getEpicTaskReference();
            EpicTask epicFromUpdatedSub = task.getEpicTaskReference();

            if (epicFromUnUpdatedSub.equals(epicFromUpdatedSub)) {
                calculateEpicTaskStatus(epicFromUpdatedSub);
            }

            subTasks.put(id, task);
        }
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicTaskById(int id) {
        if (epicTasks.containsKey(id)) {
            EpicTask epic = epicTasks.get(id);

            if (!epic.getSubtasks().isEmpty()) {
                for (int i = 0; i <= taskId; i++) {
                    SubTask subFromSubTasks = subTasks.get(i);

                    if (subFromSubTasks != null && subFromSubTasks.getEpicTaskReference().equals(epic)) {
                        subTasks.remove(i);
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

    public ArrayList<SubTask> getEpicSubTasks(EpicTask epicTask) {
        return epicTask.getSubtasks();
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
