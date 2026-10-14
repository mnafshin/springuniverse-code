package info.mnafshin.springuniverse.foundations.clientserver;

import info.mnafshin.springuniverse.foundations.http.HelloHttpServer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * F02 companion: the browser (here: {@link HttpClient}) is the client; our tiny
 * {@link HelloHttpServer} is the server. DNS is skipped because we use 127.0.0.1.
 */
public final class ClientServerDemo {

    public static void main(String[] args) throws Exception {
        try (HelloHttpServer server = HelloHttpServer.start(0)) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(
                    URI.create("http://127.0.0.1:" + server.port() + "/hello?name=Ada"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("status " + response.statusCode());
            System.out.println(response.body());
        }
    }
}
