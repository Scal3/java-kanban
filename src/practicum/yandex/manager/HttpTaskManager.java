package practicum.yandex.manager;

import com.google.gson.*;
import practicum.yandex.client.KVTaskClient;
import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

import java.io.File;

public class HttpTaskManager extends FileBackedTasksManager {
    private static final String TASKS_KEY = "TASKS_KEY";
    private static final String EPICS_KEY = "EPICS_KEY";
    private static final String SUBS_KEY = "SUBS_KEY";
    private static final String HISTORY_KEY = "HISTORY_KEY";
    private final String url;
    private final KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(String url) {
        super(new File("./tasks.csv"));
        this.url = url;
        this.gson = Managers.getGson();
        this.client = new KVTaskClient(this.url);
        this.setInitialState();
    }

    private void setInitialState() {
        int id = 0;
        JsonElement jsonElementTasks = JsonParser.parseString(client.load(TASKS_KEY));
        JsonElement jsonElementEpics = JsonParser.parseString(client.load(EPICS_KEY));
        JsonElement jsonElementSubs = JsonParser.parseString(client.load(SUBS_KEY));
        JsonElement jsonElementHistory = JsonParser.parseString(client.load(HISTORY_KEY));

        if(!jsonElementTasks.isJsonNull() && !jsonElementTasks.isJsonObject()) {
            for (JsonElement taskJson : jsonElementTasks.getAsJsonArray().asList()) {
                Task task = gson.fromJson(taskJson, Task.class);
                tasks.put(task.getId(), task);
                id = id < task.getId() ? task.getId() : id;
            }
        }

        if(!jsonElementEpics.isJsonNull() && !jsonElementEpics.isJsonObject()) {
            for (JsonElement epicJson : jsonElementEpics.getAsJsonArray().asList()) {
                EpicTask epic = gson.fromJson(epicJson, EpicTask.class);
                epicTasks.put(epic.getId(), epic);
                id = id < epic.getId() ? epic.getId() : id;
            }
        }

        if(!jsonElementSubs.isJsonNull() && !jsonElementSubs.isJsonObject()) {
            for (JsonElement subJson : jsonElementSubs.getAsJsonArray().asList()) {
                SubTask sub = gson.fromJson(subJson, SubTask.class);
                subTasks.put(sub.getId(), sub);
                id = id < sub.getId() ? sub.getId() : id;
            }
        }

        if(!jsonElementHistory.isJsonNull() && !jsonElementHistory.isJsonObject()) {
            for (JsonElement taskJson : jsonElementHistory.getAsJsonArray().asList()) {
                Task task = gson.fromJson(taskJson, Task.class);
                historyManager.add(task);
            }
        }

        taskId = id;
    }

    @Override
    public void save() {
        client.put(TASKS_KEY, gson.toJson(tasks.values()));
        client.put(EPICS_KEY, gson.toJson(epicTasks.values()));
        client.put(SUBS_KEY, gson.toJson(subTasks.values()));
        client.put(HISTORY_KEY, gson.toJson(getHistory()));
    }
}
