package info.mnafshin.springuniverse.config.lite;

import info.mnafshin.springuniverse.config.app.AuditLog;
import info.mnafshin.springuniverse.config.app.InMemoryUserRepository;
import info.mnafshin.springuniverse.config.app.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** The fix that works in every mode: ask for the dependency as a @Bean method parameter. */
@Configuration(proxyBeanMethods = false)
public class LiteFixedConfig {

    @Bean
    public AuditLog auditLog() {
        return new AuditLog();
    }

    @Bean
    public UserRepository userRepository(AuditLog auditLog) {
        return new InMemoryUserRepository(auditLog);
    }
}
