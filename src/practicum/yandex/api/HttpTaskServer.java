package practicum.yandex.api;

import com.sun.net.httpserver.HttpServer;
import practicum.yandex.manager.Managers;
import practicum.yandex.manager.TaskManager;

import java.io.File;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final int BACKLOG = 0;
    private static final String TASKS_ROUTE = "/tasks";
    private static final File TASKS_FILE = new File("./tasks.csv");

    public static void main(String[] args) {
        try {
            TaskManager manager = Managers.getFileBackedManager(TASKS_FILE);
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), BACKLOG);
            httpServer.createContext(TASKS_ROUTE, new TasksHandler(manager));
            httpServer.start();

            System.out.println("Server has started on port " + PORT);
        } catch (java.io.IOException e) {
            System.out.println("IOException");
        }
    }
}