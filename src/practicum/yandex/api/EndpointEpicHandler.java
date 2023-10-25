package practicum.yandex.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import practicum.yandex.manager.TaskManager;
import practicum.yandex.task.EpicTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static practicum.yandex.api.TasksHandler.*;

public class EndpointEpicHandler {
    private final Gson gson;
    private final TaskManager manager;

    public EndpointEpicHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    public void handleGetEpics(HttpExchange exchange) throws IOException {
        sendResponse(exchange, gson.toJson(manager.getEpicTasksValues()), RES_SUCCESS);
    }

    public void handleGetEpicById(HttpExchange exchange) throws IOException {
        if (getIdFromQuery(exchange).isEmpty()) {
            sendResponse(exchange, gson.toJson("Wrong query id"), RES_BAD_REQ);

            return;
        }

        EpicTask epic = manager.getEpicTaskById(getIdFromQuery(exchange).get());

        if (epic == null) {
            sendResponse(exchange, gson.toJson(null), RES_NOT_FOUND);
        } else {
            sendResponse(exchange, gson.toJson(epic), RES_SUCCESS);
        }
    }

    public void handleGetEpicSubs(HttpExchange exchange) throws IOException {
        if (getIdFromQuery(exchange).isEmpty()) {
            sendResponse(exchange, gson.toJson("Wrong query id"), RES_BAD_REQ);

            return;
        }

        if (manager.getEpicSubTasks(getIdFromQuery(exchange).get()) != null) {
            sendResponse(exchange, gson.toJson(manager.getEpicSubTasks(getIdFromQuery(exchange).get())), RES_SUCCESS);
        } else {
            sendResponse(exchange, gson.toJson(null), RES_NOT_FOUND);
        }
    }

    public void handleCreateEpic(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        manager.createEpicTask(gson.fromJson(body, EpicTask.class));
        sendResponse(exchange, gson.toJson("Created"), RES_CREATED);
    }

    public void handleUpdateEpic(HttpExchange exchange) throws IOException {
        if (getIdFromQuery(exchange).isEmpty()) {
            sendResponse(exchange, gson.toJson("Wrong query id"), RES_BAD_REQ);

            return;
        }

        if (manager.getEpicTaskById(getIdFromQuery(exchange).get()) == null) {
            sendResponse(exchange, gson.toJson("Epic is not found"), RES_NOT_FOUND);

            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        EpicTask epic = gson.fromJson(body, EpicTask.class);

        epic.setId(getIdFromQuery(exchange).get());
        manager.updateEpicTask(epic);
        sendResponse(exchange, gson.toJson("Updated"), RES_SUCCESS);
    }

    public void handleDeleteEpics(HttpExchange exchange) throws IOException {
        manager.deleteAllEpicTasks();
        sendResponse(exchange, gson.toJson("Deleted"), RES_SUCCESS);
    }

    public void handleDeleteEpicById(HttpExchange exchange) throws IOException {
        if (getIdFromQuery(exchange).isEmpty()) {
            sendResponse(exchange, gson.toJson("Wrong query id"), RES_BAD_REQ);

            return;
        }

        if (manager.getEpicTaskById(getIdFromQuery(exchange).get()) != null) {
            manager.deleteEpicTaskById(getIdFromQuery(exchange).get());
            sendResponse(
                    exchange,
                    gson.toJson("Deleted epic with id " + getIdFromQuery(exchange).get()),
                    RES_SUCCESS
            );
        } else {
            sendResponse(
                    exchange,
                    gson.toJson("Epic with id " + getIdFromQuery(exchange).get() + " is not found"),
                    RES_NOT_FOUND
            );
        }
    }
}
