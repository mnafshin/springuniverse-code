package info.mnafshin.springuniverse.config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A second configuration class. userRepository() calls auditLog() like a normal Java method, but because this is a
 * full @Configuration class (proxied with CGLIB), the call returns the one AuditLog singleton from the container.
 */
@Configuration
public class PersistenceConfig {

    @Bean
    public AuditLog auditLog() {
        return new AuditLog();
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository(auditLog());
    }
}
