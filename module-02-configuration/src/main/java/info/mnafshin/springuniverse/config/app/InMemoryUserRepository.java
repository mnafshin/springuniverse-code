package info.mnafshin.springuniverse.config.app;

import java.util.ArrayList;
import java.util.List;

/** No annotation: PersistenceConfig creates it with a @Bean method. */
public class InMemoryUserRepository implements UserRepository {

    private final List<String> users = new ArrayList<>();
    private final AuditLog auditLog;

    public InMemoryUserRepository(AuditLog auditLog) {
        this.auditLog = auditLog;
    }

    @Override
    public void save(String name) {
        users.add(name);
        auditLog.record("saved " + name);
    }

    @Override
    public List<String> findAll() {
        return List.copyOf(users);
    }

    public AuditLog auditLog() {
        return auditLog;
    }
}
