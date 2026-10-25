package info.mnafshin.springuniverse.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.context.ConfigurableApplicationContext;

/** The prod profile: bigger limit, the SMTP notifier, and no start without the mail password. */
class ProdProfileTest {

    @Test
    void prodWithThePasswordFromOutsideStarts() {
        try (ConfigurableApplicationContext ctx = SpringApplication.run(ConfigApplication.class,
                "--server.port=0", "--spring.profiles.active=prod", "--app.mail.password=test-only-secret")) {
            assertThat(ctx.getBean(SignupProperties.class).dailyLimit()).isEqualTo(1000);
            assertThat(ctx.getBean(SignupLimits.class).warnAt()).isEqualTo(800);
            assertThat(ctx.getBean(WelcomeNotifier.class)).isInstanceOf(SmtpWelcomeNotifier.class);
            assertThat(ctx.getBean(MailProperties.class).toString()).doesNotContain("test-only-secret");
        }
    }

    @Test
    void prodWithoutThePasswordRefusesToStart() {
        assertThatThrownBy(() -> SpringApplication.run(ConfigApplication.class,
                "--server.port=0", "--spring.profiles.active=prod").close())
                .hasRootCauseInstanceOf(BindValidationException.class)
                .rootCause().hasMessageContaining("app.mail.password");
    }

    @Test
    void lastActiveProfileWins() {
        try (ConfigurableApplicationContext ctx = SpringApplication.run(ConfigApplication.class,
                "--server.port=0", "--spring.profiles.active=prod,dev", "--app.mail.password=test-only-secret")) {
            assertThat(ctx.getBean(SignupProperties.class).dailyLimit()).isEqualTo(5);   // dev is listed last, so dev wins
        }
    }

    @Test
    void commandLineStillBeatsProfileFiles() {
        try (ConfigurableApplicationContext ctx = SpringApplication.run(ConfigApplication.class,
                "--server.port=0", "--spring.profiles.active=dev", "--app.signup.daily-limit=7")) {
            assertThat(ctx.getBean(SignupProperties.class).dailyLimit()).isEqualTo(7);
        }
    }
}
