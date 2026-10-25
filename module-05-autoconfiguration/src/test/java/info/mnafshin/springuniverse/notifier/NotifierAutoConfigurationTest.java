package info.mnafshin.springuniverse.notifier;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Each test builds a small, fresh context with {@link ApplicationContextRunner}: the
 * auto-configuration plus whatever "user configuration" and properties the case needs.
 */
class NotifierAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(NotifierAutoConfiguration.class));

    @Test
    void createsTheDefaultLogNotifier() {
        runner.run(context -> {
            assertThat(context).hasSingleBean(Notifier.class);
            assertThat(context.getBean(Notifier.class)).isInstanceOf(LogNotifier.class);
            assertThat(context).hasBean("notifier");
        });
    }

    @Test
    void backsOffWhenTheAppDefinesItsOwnNotifier() {
        runner.withUserConfiguration(UserConfig.class).run(context -> {
            assertThat(context).hasSingleBean(Notifier.class);
            assertThat(context).hasBean("smsNotifier");
            assertThat(context).doesNotHaveBean("notifier");
            assertThat(context.getBean(Notifier.class).name()).isEqualTo("sms");
        });
    }

    @Test
    void switchedOffByAProperty() {
        runner.withPropertyValues("notifier.enabled=false")
            .run(context -> assertThat(context).doesNotHaveBean(Notifier.class));
    }

    @Test
    void onWhenThePropertyIsTrue() {
        runner.withPropertyValues("notifier.enabled=true")
            .run(context -> assertThat(context).hasSingleBean(LogNotifier.class));
    }

    @Configuration(proxyBeanMethods = false)
    static class UserConfig {

        @Bean
        Notifier smsNotifier() {
            return new Notifier() {
                @Override
                public String name() {
                    return "sms";
                }

                @Override
                public String send(String to, String message) {
                    return "texted " + to;
                }
            };
        }

    }

}
