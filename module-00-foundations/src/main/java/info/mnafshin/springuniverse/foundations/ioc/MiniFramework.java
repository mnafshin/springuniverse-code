package info.mnafshin.springuniverse.foundations.ioc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Framework style (Inversion of Control): the framework owns the flow and calls YOUR code.
 * "Don't call us, we'll call you." A tiny stand-in for what Spring MVC's DispatcherServlet does.
 */
public final class MiniFramework {

    private final Map<String, RequestHandler> routes = new LinkedHashMap<>();

    /** You register your code... */
    public MiniFramework route(String path, RequestHandler handler) {
        routes.put(path, handler);
        return this;
    }

    /** ...and the framework decides when to call it. */
    public String dispatch(String path) {
        RequestHandler handler = routes.get(path);
        return handler == null ? "404 Not Found" : handler.handle(path);
    }

    /** The framework runs the show: it loops over incoming requests, not your code. */
    public List<String> run(List<String> incomingPaths) {
        return incomingPaths.stream().map(this::dispatch).toList();
    }

    public static void main(String[] args) {
        MiniFramework app = new MiniFramework()
                .route("/hello", path -> "Hello!"); // your code, called by the framework
        app.run(List.of("/hello", "/missing")).forEach(System.out::println);
    }
}
