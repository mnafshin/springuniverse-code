package info.mnafshin.springuniverse.config.lite;

import static org.assertj.core.api.Assertions.assertThat;

import info.mnafshin.springuniverse.config.app.AuditLog;
import info.mnafshin.springuniverse.config.app.InMemoryUserRepository;
import info.mnafshin.springuniverse.config.app.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class LiteConfigTest {

    @Test
    void liteModeCallCreatesASecondUnmanagedInstance() {
        try (var context = new AnnotationConfigApplicationContext(LiteConfig.class)) {
            var repository = (InMemoryUserRepository) context.getBean(UserRepository.class);

            assertThat(repository.auditLog()).isNotSameAs(context.getBean(AuditLog.class));
            assertThat(context.getBean(LiteConfig.class).getClass()).isEqualTo(LiteConfig.class);   // no CGLIB subclass
        }
    }

    @Test
    void methodParameterGivesTheSingletonInEveryMode() {
        try (var context = new AnnotationConfigApplicationContext(LiteFixedConfig.class)) {
            var repository = (InMemoryUserRepository) context.getBean(UserRepository.class);

            assertThat(repository.auditLog()).isSameAs(context.getBean(AuditLog.class));
        }
    }
}
