package info.mnafshin.springuniverse.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/** No profile set: the "default" profile applies, only application.yml is used, prod-only beans do not exist. */
@SpringBootTest
class DefaultProfileTest {

    @Autowired
    Environment environment;

    @Autowired
    ApplicationContext context;

    @Autowired
    SignupLimits limits;

    @Test
    void noActiveProfileFallsBackToDefault() {
        assertThat(environment.getActiveProfiles()).isEmpty();
        assertThat(environment.getDefaultProfiles()).containsExactly("default");
    }

    @Test
    void prodOnlyBeansAreNotCreated() {
        assertThat(context.getBeanNamesForType(MailProperties.class)).isEmpty();
        assertThat(context.getBean(WelcomeNotifier.class)).isInstanceOf(LogWelcomeNotifier.class);
    }

    @Test
    void spelAndPlaceholderDefault() {
        assertThat(limits.warnAt()).isEqualTo(80);                      // #{${app.signup.daily-limit} * 8 / 10}
        assertThat(limits.supportEmail()).isEqualTo("support@example.com");   // ${app.signup.support-email:support@example.com}
    }
}
