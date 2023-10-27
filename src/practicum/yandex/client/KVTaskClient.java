package practicum.yandex.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private static final String REGISTER_PATH = "register/";
    private static final String SAVE_PATH = "save/";
    private static final String LOAD_PATH = "load/";
    private final String url;
    private final String apiKey;
    private final HttpRequest.Builder requestBuilder;
    private final HttpClient client;
    private final HttpResponse.BodyHandler<String> bodyHandler;

    public KVTaskClient(String url) {
        this.requestBuilder = HttpRequest.newBuilder();
        this.client = HttpClient.newHttpClient();
        this.bodyHandler = HttpResponse.BodyHandlers.ofString();
        this.url = url;
        this.apiKey = "?API_TOKEN=" + register(this.url);
    }

    public void put(String key, String json) {
        // Метод должен сохранять состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN=.
        try {
            HttpRequest request = requestBuilder
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .uri(URI.create(url + SAVE_PATH + key + apiKey))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-type", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, bodyHandler);
            int status = response.statusCode();

            if (status >= 200 && status <= 299) {
                System.out.println("Сервер успешно обработал запрос. Код состояния: " + status);

            } else if (status >= 400 && status <= 499) {
                System.out.println("Сервер сообщил о проблеме с запросом. Код состояния: " + status);

            } else if (status >= 500 && status <= 599) {
                System.out.println(
                        "Сервер сообщил о внутренней проблеме и невозможности обработать запрос. "
                                + "Код состояния: "
                                + status
                );

            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + status);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL");
        }
    }

    public String load(String key) {
        // Метод должен возвращать состояние менеджера задач через запрос GET /load/<ключ>?API_TOKEN=
        String state = "";

        try {
            HttpRequest request = requestBuilder
                    .GET()
                    .uri(URI.create(url + LOAD_PATH + key + apiKey))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, bodyHandler);
            int status = response.statusCode();

            if (status >= 200 && status <= 299) {
                System.out.println("Сервер успешно обработал запрос. Код состояния: " + status);
                state = response.body();

            } else if (status >= 400 && status <= 499) {
                System.out.println("Сервер сообщил о проблеме с запросом. Код состояния: " + status);

            } else if (status >= 500 && status <= 599) {
                System.out.println(
                        "Сервер сообщил о внутренней проблеме и невозможности обработать запрос. "
                                + "Код состояния: "
                                + status
                );

            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + status);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL");
        }

        return state;
    }

    private String register(String url) {
        String key = "";

        try {
            HttpRequest request = requestBuilder
                    .GET()
                    .uri(URI.create(url + REGISTER_PATH))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, bodyHandler);
            int status = response.statusCode();

            if (status >= 200 && status <= 299) {
                System.out.println("Сервер успешно обработал запрос. Код состояния: " + status);
                key = response.body();

            } else if (status >= 400 && status <= 499) {
                System.out.println("Сервер сообщил о проблеме с запросом. Код состояния: " + status);

            } else if (status >= 500 && status <= 599) {
                System.out.println(
                        "Сервер сообщил о внутренней проблеме и невозможности обработать запрос. "
                                + "Код состояния: "
                                + status
                );

            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + status);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL");
        }

        return key;
    }
}
