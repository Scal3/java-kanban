package practicum.yandex.api;

import com.google.gson.Gson;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import practicum.yandex.manager.Managers;
import practicum.yandex.manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class TasksHandler implements HttpHandler {
    public static final int RES_SUCCESS = 200;
    public static final int RES_CREATED = 201;
    public static final int RES_NOT_FOUND = 404;
    public static final int RES_BAD_REQ = 401;
    private final TaskManager manager;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    public static void sendResponse(HttpExchange exchange, String response, int responseCode) throws IOException {
        exchange.getResponseHeaders().set("Content-type", "application/json");
        exchange.sendResponseHeaders(responseCode, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static Optional<Integer> getIdFromQuery(HttpExchange exchange) {
        int id;

        try {
            id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        return Optional.of(id);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Gson gson = Managers.getGson();
        EndpointTaskHandler endpointTaskHandler = new EndpointTaskHandler(manager, gson);
        EndpointEpicHandler endpointEpicHandler = new EndpointEpicHandler(manager, gson);
        EndpointSubTaskHandler endpointSubTaskHandler = new EndpointSubTaskHandler(manager, gson);

        switch (getEndpoint(exchange.getRequestMethod(), exchange.getRequestURI())) {
            case GET_TASKS:
                endpointTaskHandler.handleGetTasks(exchange);
            break;

            case GET_TASK_BY_ID:
                endpointTaskHandler.handleGetTaskById(exchange);
            break;

            case CREATE_TASK:
                endpointTaskHandler.handleCreateTask(exchange);
            break;

            case UPDATE_TASK:
                endpointTaskHandler.handleUpdateTask(exchange);
            break;

            case DELETE_TASKS:
                endpointTaskHandler.handleDeleteTasks(exchange);
            break;

            case DELETE_TASK_BY_ID:
                endpointTaskHandler.handleDeleteTaskById(exchange);
            break;

            case GET_EPIC_TASKS:
                endpointEpicHandler.handleGetEpics(exchange);
            break;

            case GET_EPIC_TASK_BY_ID:
                endpointEpicHandler.handleGetEpicById(exchange);
            break;

            case GET_EPIC_SUB_TASKS:
                endpointEpicHandler.handleGetEpicSubs(exchange);
            break;

            case CREATE_EPIC_TASK:
                endpointEpicHandler.handleCreateEpic(exchange);
            break;

            case UPDATE_EPIC_TASK:
                endpointEpicHandler.handleUpdateEpic(exchange);
            break;

            case DELETE_EPIC_TASKS:
                endpointEpicHandler.handleDeleteEpics(exchange);
            break;

            case DELETE_EPIC_TASK_BY_ID:
                endpointEpicHandler.handleDeleteEpicById(exchange);
            break;

            case GET_SUB_TASKS:
                endpointSubTaskHandler.handleGetSubTasks(exchange);
            break;

            case GET_SUB_TASK_BY_ID:
                endpointSubTaskHandler.handleGetSubTaskById(exchange);
            break;

            case CREATE_SUB_TASK:
                endpointSubTaskHandler.handleCreateSubTask(exchange);
            break;

            case UPDATE_SUB_TASK:
                endpointSubTaskHandler.handleUpdateSubTask(exchange);
            break;

            case DELETE_SUB_TASKS:
                endpointSubTaskHandler.handleDeleteSubTasks(exchange);
            break;

            case DELETE_SUB_TASK_BY_ID:
                endpointSubTaskHandler.handleDeleteSubTaskById(exchange);
            break;

            case GET_HISTORY:
                sendResponse(exchange, gson.toJson(manager.getHistory()), RES_SUCCESS);
            break;

            case GET_PRIORITIZED_TASKS:
                sendResponse(exchange, gson.toJson(manager.getPrioritizedTasks()), RES_SUCCESS);
                break;

            default:
                sendResponse(exchange, gson.toJson("Unknown endpoint"), RES_NOT_FOUND);
        }
    }

    private Endpoints getEndpoint(String method, URI uri) {
        if (method.equals(RequestMethods.GET.name())) {
            return handleGettingGetEndpoint(uri);

        } else if (method.equals(RequestMethods.POST.name())) {
            return handleGettingPostEndpoint(uri);

        } else if (method.equals(RequestMethods.DELETE.name())) {
            return handleGettingDeleteEndpoint(uri);

        } else {
            return Endpoints.UNKNOWN;
        }
    }

    private Endpoints handleGettingGetEndpoint(URI uri) {
        String[] pathParts = uri.getPath().split("/");
        String query = uri.getQuery();

        if (query == null) {
            if (pathParts.length == 2) {
                return Endpoints.GET_PRIORITIZED_TASKS;

            } else if (pathParts[2].equals("history") && pathParts.length == 3) {
                return Endpoints.GET_HISTORY;

            } else if (pathParts[2].equals("task") && pathParts.length == 3) {
                return Endpoints.GET_TASKS;

            } else if (pathParts[2].equals("epic") && pathParts.length == 3) {
                return Endpoints.GET_EPIC_TASKS;

            } else if (pathParts[2].equals("subtask") && pathParts.length == 3) {
                return Endpoints.GET_SUB_TASKS;

            } else {
                return Endpoints.UNKNOWN;
            }
        } else {
            boolean isId = query.split("=")[0].equals("id");

            if (isId && pathParts[2].equals("task") && pathParts.length == 3) {
                return Endpoints.GET_TASK_BY_ID;

            } else if (isId && pathParts[2].equals("epic") && pathParts.length == 3) {
                return Endpoints.GET_EPIC_TASK_BY_ID;

            } else if (isId && pathParts[2].equals("subtask") && pathParts.length == 3) {
                return  Endpoints.GET_SUB_TASK_BY_ID;

            } else if (isId && pathParts[2].equals("subtask") && pathParts[3].equals("epic") && pathParts.length == 4) {
                return Endpoints.GET_EPIC_SUB_TASKS;

            } else {
                return Endpoints.UNKNOWN;
            }
        }
    }

    private Endpoints handleGettingPostEndpoint(URI uri) {
        String[] pathParts = uri.getPath().split("/"); // pathParts[0] is empty string ""
        String query = uri.getQuery();

        if (query == null) {
            if (pathParts[2].equals("task") && pathParts.length == 3) {
                return Endpoints.CREATE_TASK;

            } else if (pathParts[2].equals("epic") && pathParts.length == 3) {
                return Endpoints.CREATE_EPIC_TASK;

            } else if (pathParts[2].equals("subtask") && pathParts.length == 3) {
                return Endpoints.CREATE_SUB_TASK;

            } else {
                return Endpoints.UNKNOWN;
            }
        } else {
            boolean isId = query.split("=")[0].equals("id");

            if (isId && pathParts[2].equals("task") && pathParts.length == 3) {
                return Endpoints.UPDATE_TASK;

            } else if (isId && pathParts[2].equals("epic") && pathParts.length == 3) {
                return Endpoints.UPDATE_EPIC_TASK;

            } else if (isId && pathParts[2].equals("subtask") && pathParts.length == 3) {
                return  Endpoints.UPDATE_SUB_TASK;

            } else {
                return Endpoints.UNKNOWN;
            }
        }
    }

    private Endpoints handleGettingDeleteEndpoint(URI uri) {
        String[] pathParts = uri.getPath().split("/"); // pathParts[0] is empty string ""
        String query = uri.getQuery();

        if (query == null) {
            if (pathParts[2].equals("task") && pathParts.length == 3) {
                return Endpoints.DELETE_TASKS;

            } else if (pathParts[2].equals("epic") && pathParts.length == 3) {
                return Endpoints.DELETE_EPIC_TASKS;

            } else if (pathParts[2].equals("subtask") && pathParts.length == 3) {
                return Endpoints.DELETE_SUB_TASKS;

            } else {
                return Endpoints.UNKNOWN;
            }
        } else {
            boolean isId = query.split("=")[0].equals("id");

            if (isId && pathParts[2].equals("task") && pathParts.length == 3) {
                return Endpoints.DELETE_TASK_BY_ID;

            } else if (isId && pathParts[2].equals("epic") && pathParts.length == 3) {
                return Endpoints.DELETE_EPIC_TASK_BY_ID;

            } else if (isId && pathParts[2].equals("subtask") && pathParts.length == 3) {
                return  Endpoints.DELETE_SUB_TASK_BY_ID;

            } else {
                return Endpoints.UNKNOWN;
            }
        }
    }
}
