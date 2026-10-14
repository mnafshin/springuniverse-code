package info.mnafshin.springuniverse.foundations.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * F04 companion: REST-style CRUD over HTTP with JDK {@link HttpServer}.
 * Resources live at /books and /books/{id}. Bodies are JSON.
 */
public final class RestBookServer implements AutoCloseable {

    private static final ObjectMapper JSON = JsonMapper.builder().build();

    private final HttpServer server;
    private final BookStore store;

    private RestBookServer(HttpServer server, BookStore store) {
        this.server = server;
        this.store = store;
    }

    public static RestBookServer start(int port) throws IOException {
        BookStore store = new BookStore();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        RestBookServer self = new RestBookServer(server, store);
        server.createContext("/books", self::handleBooks);
        server.start();
        return self;
    }

    public int port() {
        return server.getAddress().getPort();
    }

    BookStore store() {
        return store;
    }

    private void handleBooks(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        if ("/books".equals(path)) {
            switch (method) {
                case "GET" -> write(exchange, 200, store.list());
                case "POST" -> {
                    String title = readTitle(exchange);
                    write(exchange, 201, store.create(title));
                }
                default -> methodNotAllowed(exchange, "GET, POST");
            }
            return;
        }
        if (path.startsWith("/books/")) {
            long id;
            try {
                id = Long.parseLong(path.substring("/books/".length()));
            } catch (NumberFormatException e) {
                write(exchange, 400, Map.of("error", "bad id"));
                return;
            }
            switch (method) {
                case "GET" -> {
                    var book = store.get(id);
                    if (book.isPresent()) write(exchange, 200, book.get());
                    else write(exchange, 404, Map.of("error", "not found"));
                }
                case "PUT" -> {
                    String title = readTitle(exchange);
                    var book = store.put(id, title);
                    if (book.isPresent()) write(exchange, 200, book.get());
                    else write(exchange, 404, Map.of("error", "not found"));
                }
                case "DELETE" -> {
                    if (store.delete(id)) {
                        exchange.sendResponseHeaders(204, -1);
                        exchange.close();
                    } else {
                        write(exchange, 404, Map.of("error", "not found"));
                    }
                }
                default -> methodNotAllowed(exchange, "GET, PUT, DELETE");
            }
            return;
        }
        write(exchange, 404, Map.of("error", "not found"));
    }

    private static String readTitle(HttpExchange exchange) throws IOException {
        byte[] raw = exchange.getRequestBody().readAllBytes();
        JsonNode node = JSON.readTree(raw);
        JsonNode title = node.get("title");
        if (title == null || !title.isString() || title.asString().isBlank()) {
            throw new IOException("missing title");
        }
        return title.asString();
    }

    private static void write(HttpExchange exchange, int status, Object body) throws IOException {
        byte[] bytes = JSON.writeValueAsBytes(body);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    private static void methodNotAllowed(HttpExchange exchange, String allow) throws IOException {
        exchange.getResponseHeaders().add("Allow", allow);
        exchange.sendResponseHeaders(405, -1);
        exchange.close();
    }

    @Override
    public void close() {
        server.stop(0);
    }

    public static void main(String[] args) throws IOException {
        RestBookServer s = start(8080);
        System.out.println("GET/POST http://localhost:" + s.port() + "/books");
    }
}
