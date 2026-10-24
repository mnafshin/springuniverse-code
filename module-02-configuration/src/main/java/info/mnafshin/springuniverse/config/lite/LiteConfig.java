package info.mnafshin.springuniverse.config.lite;

import info.mnafshin.springuniverse.config.app.AuditLog;
import info.mnafshin.springuniverse.config.app.InMemoryUserRepository;
import info.mnafshin.springuniverse.config.app.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The trap: the same two @Bean methods in "lite" mode (no CGLIB proxy). Now auditLog() inside userRepository() is a
 * plain Java call, so a second AuditLog is created that the container doesn't manage.
 */
@Configuration(proxyBeanMethods = false)
public class LiteConfig {

    @Bean
    public AuditLog auditLog() {
        return new AuditLog();
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository(auditLog());     // don't do this in lite mode
    }
}
