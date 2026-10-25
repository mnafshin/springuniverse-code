package info.mnafshin.springuniverse.container.lifecycle;

import java.util.ArrayList;
import java.util.List;

/** Prints each lifecycle step and remembers it, so tests can check the exact order. */
public final class Events {

    private static final List<String> LOG = new ArrayList<>();

    private Events() {
    }

    public static synchronized void log(String event) {
        LOG.add(event);
        System.out.println(event);
    }

    public static synchronized List<String> all() {
        return List.copyOf(LOG);
    }

    public static synchronized void clear() {
        LOG.clear();
    }
}
