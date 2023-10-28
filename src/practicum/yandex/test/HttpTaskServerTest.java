package practicum.yandex.test;

import com.google.gson.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import practicum.yandex.adapter.DurationAdapter;
import practicum.yandex.api.HttpTaskServer;
import practicum.yandex.adapter.LocalDateTimeAdapter;
import practicum.yandex.manager.Statuses;
import practicum.yandex.task.EpicTask;
import practicum.yandex.task.SubTask;
import practicum.yandex.task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private static final HttpTaskServer server;
    private static final String BASIC_URL = "http://localhost:8080/tasks";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    static {
        try {
            server = new HttpTaskServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public static void startServer() {
        server.start();
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    // Tasks
    @Test
    public void shouldReturnTasks() throws IOException, InterruptedException {
        Task task = new Task("task", "task", Statuses.NEW.name());
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofHours(12));

        HttpRequest createTaskReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createTaskReq, handler);

        HttpRequest getTasksReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getTasksRes = client.send(getTasksReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getTasksRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertFalse(jsonArray.isEmpty());
    }

    @Test
    public void shouldReturnTaskById() throws IOException, InterruptedException {
        Task task = new Task("task", "task", Statuses.NEW.name());
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofHours(12));

        HttpRequest createTaskReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createTaskReq, handler);

        HttpRequest getTaskReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/task" + "?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getTaskRes = client.send(getTaskReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getTaskRes.body());

        assertFalse(jsonElement.isJsonNull());
    }

    @Test
    public void shouldCreateTask() throws IOException, InterruptedException {
        Task task = new Task("task", "task", Statuses.NEW.name());
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofHours(12));

        HttpRequest createTaskReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> createTasksRes = client.send(createTaskReq, handler);

        assertEquals("\"Created\"", createTasksRes.body());
    }

    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("task", "task", Statuses.NEW.name());
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofHours(12));

        HttpRequest createTaskReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createTaskReq, handler);

        task.setName("updated");
        task.setDescription("updated");
        task.setId(1);

        HttpRequest updateTaskReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create(BASIC_URL + "/task" + "?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> updateTasksRes = client.send(updateTaskReq, handler);

        assertEquals("\"Updated\"", updateTasksRes.body());
    }

    @Test
    public void shouldDeleteTasks() throws IOException, InterruptedException {
        Task task = new Task("task", "task", Statuses.NEW.name());
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofHours(12));

        HttpRequest createTaskReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createTaskReq, handler);

        HttpRequest deleteTasksReq = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(deleteTasksReq, handler);

        HttpRequest getTasksReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getTasksRes = client.send(getTasksReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getTasksRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertTrue(jsonArray.isEmpty());
    }

    @Test
    public void shouldDeleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("task", "task", Statuses.NEW.name());
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofHours(12));

        HttpRequest createTaskReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createTaskReq, handler);

        HttpRequest deleteTasksReq = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(deleteTasksReq, handler);

        HttpRequest getTaskReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/task" + "?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getTasksRes = client.send(getTaskReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getTasksRes.body());

        assertTrue(jsonElement.isJsonNull());
    }

    // Epics
    @Test
    public void shouldReturnEpics() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("sub", "sub", Statuses.NEW.name(), 1);
        subTask.setStartTime(LocalDateTime.now());
        subTask.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(subTask));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        HttpRequest getEpicsReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getEpicsRes = client.send(getEpicsReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getEpicsRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertFalse(jsonArray.isEmpty());
    }

    @Test
    public void shouldReturnEpicById() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("sub", "sub", Statuses.NEW.name(), 1);
        subTask.setStartTime(LocalDateTime.now());
        subTask.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(subTask));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        HttpRequest getEpicReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/epic" + "?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getEpicRes = client.send(getEpicReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getEpicRes.body());

        assertFalse(jsonElement.isJsonNull());
    }

    @Test
    public void shouldReturnEpicsSubs() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("sub", "sub", Statuses.NEW.name(), 1);
        subTask.setStartTime(LocalDateTime.now());
        subTask.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(subTask));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        HttpRequest getEpicsSubsReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/subtask" + "/epic" + "?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getEpicsSubsRes = client.send(getEpicsSubsReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getEpicsSubsRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertFalse(jsonArray.isEmpty());
    }

    @Test
    public void shouldCreateEpic() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("sub", "sub", Statuses.NEW.name(), 1);
        subTask.setStartTime(LocalDateTime.now());
        subTask.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(subTask));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        HttpRequest getEpicsReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getEpicsRes = client.send(getEpicsReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getEpicsRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertFalse(jsonArray.isEmpty());
    }

    @Test
    public void shouldUpdateEpic() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("sub", "sub", Statuses.NEW.name(), 1);
        subTask.setStartTime(LocalDateTime.now());
        subTask.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(subTask));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        epic.setName("updated");
        epic.setDescription("updated");
        epic.setId(1);

        HttpRequest updateEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic" + "?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> updateEpicRes = client.send(updateEpicReq, handler);

        assertEquals("\"Updated\"", updateEpicRes.body());
    }

    @Test
    public void shouldDeleteEpics() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("sub", "sub", Statuses.NEW.name(), 1);
        subTask.setStartTime(LocalDateTime.now());
        subTask.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(subTask));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        HttpRequest deleteEpicsReq = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(deleteEpicsReq, handler);

        HttpRequest getEpicsReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getEpicsRes = client.send(getEpicsReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getEpicsRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertTrue(jsonArray.isEmpty());
    }

    @Test
    public void shouldDeleteEpicById() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("sub", "sub", Statuses.NEW.name(), 1);
        subTask.setStartTime(LocalDateTime.now());
        subTask.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(subTask));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        HttpRequest deleteEpicReq = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASIC_URL + "/epic" + "?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(deleteEpicReq, handler);

        HttpRequest getEpicsReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getEpicsRes = client.send(getEpicsReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getEpicsRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertTrue(jsonArray.isEmpty());
    }

    // Subs
    @Test
    public void shouldReturnSubs() throws IOException, InterruptedException {
        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setStartTime(LocalDateTime.now().plusDays(20));
        sub1.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(sub1));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.NEW.name(), 1);
        sub2.setStartTime(LocalDateTime.now());
        sub2.setDuration(Duration.ofHours(1));

        HttpRequest createSubReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(sub2)))
                .uri(URI.create(BASIC_URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createSubReq, handler);

        HttpRequest getSubsReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getSubsRes = client.send(getSubsReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getSubsRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertFalse(jsonArray.isEmpty());
    }

    @Test
    public void shouldReturnSubById() throws IOException, InterruptedException {
        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setStartTime(LocalDateTime.of(2022, Month.APRIL, 20, 10, 10));
        sub1.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(sub1));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.NEW.name(), 1);
        sub2.setStartTime(LocalDateTime.of(2023, Month.APRIL, 20, 10, 10));
        sub2.setDuration(Duration.ofHours(1));

        HttpRequest createSubReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(sub2)))
                .uri(URI.create(BASIC_URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createSubReq, handler);

        HttpRequest getSubReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/subtask" + "?id=3"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getSubRes = client.send(getSubReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getSubRes.body());

        assertFalse(jsonElement.isJsonNull());
    }

    @Test
    public void shouldCreateSub() throws IOException, InterruptedException {
        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setStartTime(LocalDateTime.now().plusDays(20));
        sub1.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(sub1));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.NEW.name(), 1);
        sub2.setStartTime(LocalDateTime.now());
        sub2.setDuration(Duration.ofHours(1));

        HttpRequest createSubReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(sub2)))
                .uri(URI.create(BASIC_URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createSubReq, handler);

        HttpRequest getSubsReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getSubsRes = client.send(getSubsReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getSubsRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertFalse(jsonArray.isEmpty());
    }

    @Test
    public void shouldUpdateSub() throws IOException, InterruptedException {
        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setStartTime(LocalDateTime.of(2022, Month.APRIL, 20, 10, 10));
        sub1.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(sub1));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.NEW.name(), 1);
        sub2.setStartTime(LocalDateTime.of(2023, Month.APRIL, 20, 10, 10));
        sub2.setDuration(Duration.ofHours(1));

        HttpRequest createSubReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(sub2)))
                .uri(URI.create(BASIC_URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createSubReq, handler);

        sub2.setName("updated");
        sub2.setDescription("updated");
        sub2.setId(3);

        HttpRequest updateSubReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(sub2)))
                .uri(URI.create(BASIC_URL + "/subtask" + "?id=3"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> updateSubRes = client.send(updateSubReq, handler);

        assertEquals("\"Updated\"", updateSubRes.body());
    }

    @Test
    public void shouldDeleteSubs() throws IOException, InterruptedException {
        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setStartTime(LocalDateTime.now().plusDays(20));
        sub1.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(sub1));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.NEW.name(), 1);
        sub2.setStartTime(LocalDateTime.now());
        sub2.setDuration(Duration.ofHours(1));

        HttpRequest createSubReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(sub2)))
                .uri(URI.create(BASIC_URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createSubReq, handler);

        HttpRequest deleteSubsReq = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASIC_URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(deleteSubsReq, handler);

        HttpRequest getSubsReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getSubsRes = client.send(getSubsReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getSubsRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertTrue(jsonArray.isEmpty());
    }

    @Test
    public void shouldDeleteSubById() throws IOException, InterruptedException {
        SubTask sub1 = new SubTask("sub1", "sub1", Statuses.NEW.name(), 1);
        sub1.setStartTime(LocalDateTime.now().plusDays(20));
        sub1.setDuration(Duration.ofHours(1));

        EpicTask epic = new EpicTask("epic", "epic", Statuses.NEW.name(), List.of(sub1));

        HttpRequest createEpicReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(BASIC_URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createEpicReq, handler);

        SubTask sub2 = new SubTask("sub2", "sub2", Statuses.NEW.name(), 1);
        sub2.setStartTime(LocalDateTime.now());
        sub2.setDuration(Duration.ofHours(1));

        HttpRequest createSubReq = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(sub2)))
                .uri(URI.create(BASIC_URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createSubReq, handler);

        HttpRequest deleteSubReq = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASIC_URL + "/subtask" + "?id=3"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(deleteSubReq, handler);

        HttpRequest getSubsReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getSubsRes = client.send(getSubsReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getSubsRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(1, jsonArray.asList().size());
    }

    // History
    @Test
    public void shouldReturnHistory() throws IOException, InterruptedException {
        Task task1 = new Task("task1", "task1", Statuses.NEW.name());
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofHours(1));

        HttpRequest createTask1Req = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createTask1Req, handler);

        HttpRequest getTaskReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/task" + "?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(getTaskReq, handler);

        HttpRequest getHistoryReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL + "/history"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getTasksRes = client.send(getHistoryReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getTasksRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertFalse(jsonArray.isEmpty());
    }

    // Prioritized tasks
    @Test
    public void shouldReturnPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("task1", "task1", Statuses.NEW.name());
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofHours(1));

        HttpRequest createTask1Req = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createTask1Req, handler);

        Task task2 = new Task("task2", "task2", Statuses.NEW.name());
        task2.setStartTime(LocalDateTime.now().plusDays(1));
        task2.setDuration(Duration.ofHours(1));

        HttpRequest createTask2Req = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2)))
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createTask2Req, handler);

        Task task3 = new Task("task2", "task2", Statuses.NEW.name());
        task3.setStartTime(LocalDateTime.now().plusDays(2));
        task3.setDuration(Duration.ofHours(1));

        HttpRequest createTask3Req = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task3)))
                .uri(URI.create(BASIC_URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(createTask3Req, handler);

        HttpRequest getPrioritizedTasksReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> getPrioritizedTasksRes = client.send(getPrioritizedTasksReq, handler);

        JsonElement jsonElement = JsonParser.parseString(getPrioritizedTasksRes.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(3, jsonArray.asList().size());
    }
}