package practicum.yandex.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import practicum.yandex.manager.TaskManager;
import practicum.yandex.task.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static practicum.yandex.api.TasksHandler.*;

public class EndpointTaskHandler {
    private final Gson gson;
    private final TaskManager manager;

    public EndpointTaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    public void handleGetTasks(HttpExchange exchange) throws IOException {
        sendResponse(exchange, gson.toJson(manager.getTasksValues()), RES_SUCCESS);
    }

    public void handleGetTaskById(HttpExchange exchange) throws IOException {
        if (getIdFromQuery(exchange).isEmpty()) {
            sendResponse(exchange, gson.toJson("Wrong query id"), RES_BAD_REQ);

            return;
        }

        Task task = manager.getTaskById(getIdFromQuery(exchange).get());

        if (task != null) {
            sendResponse(exchange, gson.toJson(task), RES_SUCCESS);
        } else {
            sendResponse(exchange, gson.toJson(null), RES_NOT_FOUND);
        }
    }

    public void handleCreateTask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        manager.createTask(gson.fromJson(body, Task.class));
        sendResponse(exchange, gson.toJson("Created"), RES_CREATED);
    }

    public void handleUpdateTask(HttpExchange exchange) throws IOException {
        if (getIdFromQuery(exchange).isEmpty()) {
            sendResponse(exchange, gson.toJson("Wrong query id"), RES_BAD_REQ);

            return;
        }

        if (manager.getTaskById(getIdFromQuery(exchange).get()) == null) {
            sendResponse(exchange, gson.toJson("Task is not found"), RES_NOT_FOUND);

            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        Task task = gson.fromJson(body, Task.class);
        task.setId(getIdFromQuery(exchange).get());
        manager.updateTask(task);
        sendResponse(exchange, gson.toJson("Updated"), RES_SUCCESS);
    }

    public void handleDeleteTasks(HttpExchange exchange) throws IOException {
        manager.deleteAllTasks();
        sendResponse(exchange, gson.toJson("Deleted"), RES_SUCCESS);
    }

    public void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        if (getIdFromQuery(exchange).isEmpty()) {
            sendResponse(exchange, gson.toJson("Wrong query id"), RES_BAD_REQ);

            return;
        }

        if (manager.getTaskById(getIdFromQuery(exchange).get()) != null) {
            manager.deleteTaskById(getIdFromQuery(exchange).get());
            sendResponse(
                    exchange,
                    gson.toJson("Deleted task with id " + getIdFromQuery(exchange).get()),
                    RES_SUCCESS
            );
        } else {
            sendResponse(
                    exchange,
                    gson.toJson("Task with id " + getIdFromQuery(exchange).get() + " is not found"),
                    RES_NOT_FOUND
            );
        }
    }

    private Optional<JsonObject> parseBody(String body) {
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject;

        if (jsonElement.isJsonObject()) {
            jsonObject = jsonElement.getAsJsonObject();
        } else {
            return Optional.empty();
        }

        if (!jsonObject.has("name")) {
            return Optional.empty();

        } else if (!jsonObject.has("description")) {
            return Optional.empty();

        } else if (!jsonObject.has("status")) {
            return Optional.empty();

        } else {
            return Optional.of(jsonObject);
        }
    }
}