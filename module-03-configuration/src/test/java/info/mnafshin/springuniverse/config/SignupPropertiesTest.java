package info.mnafshin.springuniverse.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.SystemEnvironmentPropertySource;

/** Binding rules for the record, without starting the web app. */
class SignupPropertiesTest {

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(SignupProperties.class)
    static class Config {
    }

    private final ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Config.class);

    @Test
    void kebabCaseKeysBindToTheRecordComponents() {
        runner.withPropertyValues("app.signup.welcome-message=Hi", "app.signup.daily-limit=5", "app.signup.link-expiry=2h")
                .run(ctx -> assertThat(ctx.getBean(SignupProperties.class))
                        .isEqualTo(new SignupProperties("Hi", 5, Duration.ofHours(2))));
    }

    @Test
    void missingLinkExpiryFallsBackToTheDefaultValue() {
        runner.withPropertyValues("app.signup.welcome-message=Hi", "app.signup.daily-limit=5")
                .run(ctx -> assertThat(ctx.getBean(SignupProperties.class).linkExpiry()).isEqualTo(Duration.ofMinutes(30)));
    }

    @Test
    void environmentVariableNamesUseUppercaseUnderscoresAndNoDashes() {
        runner.withPropertyValues("app.signup.welcome-message=Hi", "app.signup.daily-limit=5")
                .withInitializer(ctx -> ctx.getEnvironment().getPropertySources().addFirst(
                        new SystemEnvironmentPropertySource("systemEnvironment", Map.of("APP_SIGNUP_DAILYLIMIT", "50"))))
                .run(ctx -> assertThat(ctx.getBean(SignupProperties.class).dailyLimit()).isEqualTo(50));
    }

    @Test
    void invalidValuesStopTheAppAtStartup() {
        runner.withPropertyValues("app.signup.welcome-message=Hi", "app.signup.daily-limit=0")
                .run(ctx -> {
                    assertThat(ctx).hasFailed();
                    assertThat(ctx.getStartupFailure())
                            .rootCause()
                            .isInstanceOf(BindValidationException.class)
                            .hasMessageContaining("dailyLimit");
                });
    }

    @Test
    void aBlankWelcomeMessageIsRejected() {
        runner.withPropertyValues("app.signup.welcome-message=", "app.signup.daily-limit=5")
                .run(ctx -> assertThat(ctx).hasFailed());
    }
}
