package practicum.yandex.api;

import com.sun.net.httpserver.HttpServer;
import practicum.yandex.manager.Managers;
import practicum.yandex.manager.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final int BACKLOG = 0;
    private static final String TASKS_ROUTE = "/tasks";
    private static final File TASKS_FILE = new File("./tasks.csv");
    private final TaskManager manager;
    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        this.manager = Managers.getFileBackedManager(TASKS_FILE);
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), BACKLOG);
        this.httpServer.createContext(TASKS_ROUTE, new TasksHandler(this.manager));
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }

    public void start() {
        httpServer.start();
        System.out.println("Server has started on port " + PORT);
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Server has stopped");
    }
}