package practicum.yandex.manager;

import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private static final String SAVE_FILE_EXCEPTION = "Ошибка сохранения файла";
    private static final String FILE_HEADER = "id,type,name,status,description,epic";
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        List<Task> tasks = new ArrayList<>();
        List<Integer> history = new ArrayList<>();

        try (
                Reader fileReader = new FileReader(file, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(fileReader);
        ) {
            while (br.ready()) {
                String line = br.readLine();

                if (line.contains(FILE_HEADER) || line.equals("")) continue;

                if (
                        line.contains(TaskTypes.TASK.name())
                                || line.contains(TaskTypes.EPIC.name())
                                || line.contains(TaskTypes.SUBTASK.name())
                ) {
                    tasks.add(createTaskFromString(line));
                } else {
                    history = createHistoryFromString(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Task task : tasks) {
            if (task instanceof EpicTask) {
                manager.createEpicTask((EpicTask) task);
            } else if (task instanceof SubTask) {
                manager.createSubTask((SubTask) task);
            } else {
                manager.createTask(task);
            }
        }

        for (Integer id : history) {
            if (manager.getTaskById(id) != null) {
                manager.addToTasksHistory(manager.getTaskById(id));
            } else if (manager.getEpicTaskById(id) != null) {
                manager.addToTasksHistory(manager.getEpicTaskById(id));
            } else {
                manager.addToTasksHistory(manager.getSubTaskById(id));
            }
        }

        return manager;
    }

    private static Task createTaskFromString(String line) {
        String[] lineArr = line.split(",");

        if (lineArr[1].equals(TaskTypes.TASK.name())) {
            Task task = new Task(lineArr[2], lineArr[4], lineArr[3]);
            task.setId(Integer.parseInt(lineArr[0]));

            return task;
        } else if (lineArr[1].equals(TaskTypes.EPIC.name())) {
            EpicTask task = new EpicTask(lineArr[2], lineArr[4], lineArr[3], new ArrayList<>());
            task.setId(Integer.parseInt(lineArr[0]));

            return task;
        } else {
            SubTask task = new SubTask(lineArr[2], lineArr[4], lineArr[3], null);
            task.setId(Integer.parseInt(lineArr[0]));

            return task;
        }
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

    @Override
    protected void addToTasksHistory(Task task) {
        super.addToTasksHistory(task);
        save();
    }

    @Override
    protected void removeFromTasksHistory(int id) {
        super.removeFromTasksHistory(id);
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
}