package info.mnafshin.springuniverse.foundations.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

class HelloHttpServerTest {

    private HelloHttpServer server;
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void start() throws Exception {
        server = HelloHttpServer.start(0); // random free port
    }

    @AfterEach
    void stop() {
        server.close();
    }

    private HttpResponse<String> send(HttpRequest.Builder request) throws Exception {
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }

    private URI uri(String pathAndQuery) {
        return URI.create("http://127.0.0.1:" + server.port() + pathAndQuery);
    }

    @Test
    void getReturnsJson() throws Exception {
        HttpResponse<String> response = send(HttpRequest.newBuilder(uri("/hello?name=Ada")));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("Content-Type")).hasValue("application/json");
        Greeting greeting = JsonMapper.builder().build().readValue(response.body(), Greeting.class);
        assertThat(greeting).isEqualTo(new Greeting("Hello, Ada!", 11));
    }

    @Test
    void defaultsToWorld() throws Exception {
        assertThat(send(HttpRequest.newBuilder(uri("/hello"))).body()).contains("Hello, World!");
    }

    @Test
    void postIsNotAllowed() throws Exception {
        HttpResponse<String> response = send(HttpRequest.newBuilder(uri("/hello")).POST(HttpRequest.BodyPublishers.noBody()));

        assertThat(response.statusCode()).isEqualTo(405);
        assertThat(response.headers().firstValue("Allow")).hasValue("GET");
    }

    @Test
    void decodesQueryParameters() {
        assertThat(HelloHttpServer.queryParam("a=1&name=Grace%20Hopper", "name", "x")).isEqualTo("Grace Hopper");
    }
}
