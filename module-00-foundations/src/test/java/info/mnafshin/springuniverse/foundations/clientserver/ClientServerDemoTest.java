package info.mnafshin.springuniverse.foundations.clientserver;

import info.mnafshin.springuniverse.foundations.http.HelloHttpServer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ClientServerDemoTest {

    @Test
    void clientFetchesJsonFromLocalServer() throws Exception {
        try (HelloHttpServer server = HelloHttpServer.start(0)) {
            HttpResponse<String> response = HttpClient.newHttpClient().send(
                    HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + server.port() + "/hello?name=Ada")).GET().build(),
                    HttpResponse.BodyHandlers.ofString());
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(response.body()).contains("\"message\":\"Hello, Ada!\"");
        }
    }
}
