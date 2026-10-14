package info.mnafshin.springuniverse.foundations.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * A minimal HTTP server with the JDK's built-in {@link HttpServer} (no framework):
 * GET /hello?name=Ada responds 200 with a JSON body. Anything other than GET is 405.
 */
public final class HelloHttpServer implements AutoCloseable {

    private static final ObjectMapper JSON = JsonMapper.builder().build();

    private final HttpServer server;

    private HelloHttpServer(HttpServer server) {
        this.server = server;
    }

    /** Starts on the given port; 0 picks a free port. */
    public static HelloHttpServer start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        server.createContext("/hello", HelloHttpServer::handle);
        server.start();
        return new HelloHttpServer(server);
    }

    public int port() {
        return server.getAddress().getPort();
    }

    private static void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Allow", "GET");
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }
        String name = queryParam(exchange.getRequestURI().getRawQuery(), "name", "World");
        String message = "Hello, " + name + "!";
        byte[] body = JSON.writeValueAsBytes(new Greeting(message, message.length()));
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(body);
        }
    }

    static String queryParam(String rawQuery, String key, String fallback) {
        if (rawQuery == null) {
            return fallback;
        }
        for (String pair : rawQuery.split("&")) {
            int eq = pair.indexOf('=');
            if (eq > 0 && pair.substring(0, eq).equals(key)) {
                return URLDecoder.decode(pair.substring(eq + 1), StandardCharsets.UTF_8);
            }
        }
        return fallback;
    }

    @Override
    public void close() {
        server.stop(0);
    }

    public static void main(String[] args) throws IOException {
        HelloHttpServer s = start(8080);
        System.out.println("Try: curl 'http://localhost:" + s.port() + "/hello?name=Ada'");
    }
}
