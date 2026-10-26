package info.mnafshin.springuniverse.container.beanlife.order;

import java.util.HashSet;
import java.util.Set;

/** Stands in for a real database: just remembers which tables exist. */
public final class Database {

    private static final Set<String> TABLES = new HashSet<>();

    private Database() {
    }

    public static synchronized void createTable(String name) {
        TABLES.add(name);
    }

    public static synchronized boolean hasTable(String name) {
        return TABLES.contains(name);
    }

    public static synchronized void reset() {
        TABLES.clear();
    }
}
