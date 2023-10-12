package practicum.yandex.manager;

import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;
import practicum.yandex.task.TaskTypes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String SAVE_FILE_EXCEPTION = "Save file exception";
    private static final String FILE_HEADER = "id,type,name,status,description,startTime,duration,epic";
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        File file = new File("./test.csv");
        TaskManager manager = new FileBackedTasksManager(file);
        Task task1 = new Task("task1", "task1", "NEW");
        Task task2 = new Task("task2", "task2", "NEW");
        List<SubTask> subs = new ArrayList<>();
        EpicTask epic = new EpicTask("epic", "epic", "NEW", subs);
        SubTask sub1 = new SubTask("sub1", "sub1", "DONE", null);
        SubTask sub2 = new SubTask("sub2", "sub2", "DONE", null);

        subs.add(sub1);
        subs.add(sub2);

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpicTask(epic);

        manager.getTaskById(1);
        manager.getTaskById(2);

        TaskManager manager2 = FileBackedTasksManager.loadFromFile(file);
        System.out.println(manager2.getEpicTasksValues());
        System.out.println(manager2.getTasksValues());
        System.out.println(manager2.getSubTasksValues());
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        int taskId = 0;
        List<Task> tasks = new ArrayList<>();
        List<Integer> history = new ArrayList<>();

        try (
                Reader fileReader = new FileReader(file, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(fileReader)
        ) {
            while (br.ready()) {
                String line = br.readLine();

                if (line.contains(FILE_HEADER) || line.isEmpty()) continue;

                if (
                        line.contains(TaskTypes.TASK.name())
                                || line.contains(TaskTypes.EPIC.name())
                                || line.contains(TaskTypes.SUBTASK.name())
                ) {
                    tasks.add(manager.createTaskFromString(line));
                } else {
                    history = createHistoryFromString(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Task task : tasks) {
            if (task.getType().equals(TaskTypes.EPIC.name())) {
                manager.epicTasks.put(task.getId(), (EpicTask) task);
            } else if (task.getType().equals(TaskTypes.SUBTASK.name())) {
                manager.subTasks.put(task.getId(), (SubTask) task);
            } else {
                manager.tasks.put(task.getId(), task);
            }

            taskId = taskId < task.getId() ? task.getId() : taskId;
        }

        for (SubTask sub : manager.subTasks.values()) {
            EpicTask epic = manager.epicTasks.get(sub.getEpicId());
            epic.getSubtasks().add(sub);
        }

        for (Integer id : history) {
            if (manager.tasks.containsKey(id)) {
                manager.historyManager.add(manager.tasks.get(id));
            } else if (manager.epicTasks.containsKey(id)) {
                manager.historyManager.add(manager.epicTasks.get(id));
            } else {
                manager.historyManager.add(manager.subTasks.get(id));
            }
        }

        manager.taskId = taskId;

        return manager;
    }

    private static List<Integer> createHistoryFromString(String line) {
        String[] idArr = line.split(",");
        List<Integer> history = new ArrayList<>();

        for (String item : idArr) {
            history.add(Integer.parseInt(item));
        }

        return history;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();

        return task;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        EpicTask task = super.getEpicTaskById(id);
        save();

        return task;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask task = super.getSubTaskById(id);
        save();

        return task;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpicTask(EpicTask task) {
        super.createEpicTask(task);
        save();
    }

    @Override
    public void createSubTask(SubTask task) {
        super.createSubTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask task) {
        super.updateEpicTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask task) {
        super.updateSubTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicTaskById(int id) {
        super.deleteEpicTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    private void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(file.getAbsoluteFile(), StandardCharsets.UTF_8)) {
            fileWriter.write(createCsvString());
        } catch (IOException e) {
            throw new ManagerSaveException(SAVE_FILE_EXCEPTION);
        }
    }

    private String createCsvString() {
        StringBuilder builder = new StringBuilder(FILE_HEADER + "\n");

        for (Task task : super.getTasksValues()) {
            builder.append(task).append("\n");
        }

        for (EpicTask task : super.getEpicTasksValues()) {
            builder.append(task).append("\n");
        }

        for (SubTask task : super.getSubTasksValues()) {
            builder.append(task).append("\n");
        }

        builder.append("\n");

        if (super.getHistory() != null) {
            for (Task task : super.getHistory()) {
                builder.append(task.getId()).append(",");
            }
        }

        return builder.toString();
    }

    private Task createTaskFromString(String line) {
        String[] lineArr = line.split(",");

        if (lineArr[1].equals(TaskTypes.TASK.name())) {
            Task task = new Task(lineArr[2], lineArr[4], lineArr[3]);
            task.setId(Integer.parseInt(lineArr[0]));
            task.setStartTime(LocalDateTime.parse(lineArr[5]));
            task.setDuration(Duration.parse(lineArr[6]));

            return task;
        } else if (lineArr[1].equals(TaskTypes.EPIC.name())) {
            EpicTask task = new EpicTask(lineArr[2], lineArr[4], lineArr[3], new ArrayList<>());
            task.setId(Integer.parseInt(lineArr[0]));

            return task;
        } else {
            SubTask task = new SubTask(lineArr[2], lineArr[4], lineArr[3], Integer.parseInt(lineArr[7]));
            task.setId(Integer.parseInt(lineArr[0]));
            task.setStartTime(LocalDateTime.parse(lineArr[5]));
            task.setDuration(Duration.parse(lineArr[6]));

            return task;
        }
    }
}
