package info.mnafshin.springuniverse.config.app;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class AppConfigTest {

    @Test
    void containerWiresScannedComponentsAndBeanMethods() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            SignupService signup = context.getBean(SignupService.class);

            assertThat(signup.register("Ada")).startsWith("Welcome, Ada! (signed up ");
            assertThat(context.getBean(UserRepository.class).findAll()).containsExactly("Ada");
        }
    }

    @Test
    void beanNameIsTheMethodNameOrTheClassName() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            assertThat(context.getBean("clock")).isInstanceOf(Clock.class);              // @Bean method name
            assertThat(context.getBean("emailNotifier")).isInstanceOf(EmailNotifier.class); // scanned class, lower camel case
            assertThat(context.getBeanDefinitionNames())
                    .contains("appConfig", "clock", "auditLog", "userRepository", "emailNotifier", "signupService");
        }
    }

    @Test
    void configurationClassIsACglibSubclass() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            assertThat(context.getBean(AppConfig.class).getClass().getName())
                    .startsWith(AppConfig.class.getName() + "$$SpringCGLIB$$");
        }
    }

    @Test
    void callingABeanMethodTwiceStillGivesOneSingleton() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            var repository = (InMemoryUserRepository) context.getBean(UserRepository.class);

            assertThat(repository.auditLog()).isSameAs(context.getBean(AuditLog.class));
            assertThat(context.getBeansOfType(AuditLog.class)).hasSize(1);
        }
    }

    @Test
    void oneConfigClassAloneOnlyKnowsItsOwnBeans() {
        try (var context = new AnnotationConfigApplicationContext(PersistenceConfig.class)) {
            assertThat(context.getBeanNamesForType(UserRepository.class)).containsExactly("userRepository");
            assertThat(context.getBeanNamesForType(SignupService.class)).isEmpty();
        }
    }
}
