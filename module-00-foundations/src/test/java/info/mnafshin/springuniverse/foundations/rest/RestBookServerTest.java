package info.mnafshin.springuniverse.foundations.rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class RestBookServerTest {

    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    void crudRoundTrip() throws Exception {
        try (RestBookServer server = RestBookServer.start(0)) {
            String base = "http://127.0.0.1:" + server.port();

            HttpResponse<String> created = client.send(
                    HttpRequest.newBuilder(URI.create(base + "/books"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"My First API\"}", StandardCharsets.UTF_8))
                            .build(),
                    HttpResponse.BodyHandlers.ofString());
            assertThat(created.statusCode()).isEqualTo(201);
            assertThat(created.body()).contains("\"title\":\"My First API\"");
            assertThat(created.body()).contains("\"id\":1");

            HttpResponse<String> got = client.send(
                    HttpRequest.newBuilder(URI.create(base + "/books/1")).GET().build(),
                    HttpResponse.BodyHandlers.ofString());
            assertThat(got.statusCode()).isEqualTo(200);
            assertThat(got.body()).contains("My First API");

            HttpResponse<String> updated = client.send(
                    HttpRequest.newBuilder(URI.create(base + "/books/1"))
                            .header("Content-Type", "application/json")
                            .PUT(HttpRequest.BodyPublishers.ofString("{\"title\":\"Updated\"}", StandardCharsets.UTF_8))
                            .build(),
                    HttpResponse.BodyHandlers.ofString());
            assertThat(updated.statusCode()).isEqualTo(200);
            assertThat(updated.body()).contains("Updated");

            HttpResponse<String> deleted = client.send(
                    HttpRequest.newBuilder(URI.create(base + "/books/1")).DELETE().build(),
                    HttpResponse.BodyHandlers.ofString());
            assertThat(deleted.statusCode()).isEqualTo(204);

            HttpResponse<String> missing = client.send(
                    HttpRequest.newBuilder(URI.create(base + "/books/1")).GET().build(),
                    HttpResponse.BodyHandlers.ofString());
            assertThat(missing.statusCode()).isEqualTo(404);
        }
    }
}
