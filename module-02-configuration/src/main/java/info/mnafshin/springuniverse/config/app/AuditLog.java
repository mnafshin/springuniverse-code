package info.mnafshin.springuniverse.config.app;

import java.util.ArrayList;
import java.util.List;

/** Shared by several beans, so there must be exactly one. The constructor prints, so you can count instances. */
public class AuditLog {

    private final List<String> entries = new ArrayList<>();

    public AuditLog() {
        System.out.println("AuditLog created");
    }

    public void record(String entry) {
        entries.add(entry);
    }

    public List<String> entries() {
        return List.copyOf(entries);
    }
}
