package practicum.yandex.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import practicum.yandex.manager.TaskManager;
import practicum.yandex.task.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static practicum.yandex.api.TasksHandler.*;
import static practicum.yandex.api.TasksHandler.RES_BAD_REQ;

public class EndpointSubTaskHandler {
    private final Gson gson;
    private final TaskManager manager;

    public EndpointSubTaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    public void handleGetSubTasks(HttpExchange exchange) throws IOException {
        sendResponse(exchange, gson.toJson(manager.getSubTasksValues()), RES_SUCCESS);
    }

    public void handleGetSubTaskById(HttpExchange exchange) throws IOException {
        if (getIdFromQuery(exchange).isEmpty()) {
            sendResponse(exchange, gson.toJson("Wrong query id"), RES_BAD_REQ);

            return;
        }

        SubTask subTask = manager.getSubTaskById(getIdFromQuery(exchange).get());

        if (subTask != null) {
            sendResponse(exchange, gson.toJson(subTask), RES_SUCCESS);
        } else {
            sendResponse(exchange, gson.toJson(null), RES_NOT_FOUND);
        }
    }

    public void handleCreateSubTask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        SubTask subTask = gson.fromJson(body, SubTask.class);
        manager.createSubTask(subTask);
        sendResponse(exchange, gson.toJson("Created"), RES_CREATED);
    }

    public void handleUpdateSubTask(HttpExchange exchange) throws IOException {
        if (getIdFromQuery(exchange).isEmpty()) {
            sendResponse(exchange, gson.toJson("Wrong query id"), RES_BAD_REQ);

            return;
        }

        if (manager.getSubTaskById(getIdFromQuery(exchange).get()) == null) {
            sendResponse(exchange, gson.toJson("Subtask is not found"), RES_NOT_FOUND);

            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        SubTask subTask = gson.fromJson(body, SubTask.class);

        subTask.setId(getIdFromQuery(exchange).get());
        manager.updateSubTask(subTask);
        sendResponse(exchange, gson.toJson("Updated"), RES_SUCCESS);
    }

    public void handleDeleteSubTasks(HttpExchange exchange) throws IOException {
        manager.deleteAllSubTasks();
        sendResponse(exchange, gson.toJson("Deleted"), RES_SUCCESS);
    }

    public void handleDeleteSubTaskById(HttpExchange exchange) throws IOException {
        if (getIdFromQuery(exchange).isEmpty()) {
            sendResponse(exchange, gson.toJson("Wrong query id"), RES_BAD_REQ);

            return;
        }

        if (manager.getSubTaskById(getIdFromQuery(exchange).get()) != null) {
            manager.deleteSubTaskById(getIdFromQuery(exchange).get());

            sendResponse(
                    exchange,
                    gson.toJson("Deleted subtask with id " + getIdFromQuery(exchange).get()),
                    RES_SUCCESS
            );
        } else {
            sendResponse(
                    exchange,
                    gson.toJson("Subtask with id " + getIdFromQuery(exchange).get() + " is not found"),
                    RES_NOT_FOUND
            );
        }
    }
}
