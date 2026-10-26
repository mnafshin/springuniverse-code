package info.mnafshin.springuniverse.greeting;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * T5.2 Example 5, the fix: one {@link ApplicationContextRunner}, three small contexts. The
 * auto-configuration goes in with {@code withConfiguration(AutoConfigurations.of(...))}, so it is
 * processed after the user configuration, just like in a real app.
 */
class GreetingAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(GreetingAutoConfiguration.class));

    @Test
    void defaultGreeter() {
        runner.run(context -> {
            assertThat(context).hasSingleBean(Greeter.class);
            assertThat(context.getBean(Greeter.class).greet("Ada")).isEqualTo("Hello, Ada!");
        });
    }

    @Test
    void backsOffForYourOwnGreeter() {
        runner.withUserConfiguration(MyGreeterConfig.class).run(context -> {
            assertThat(context).hasSingleBean(Greeter.class);
            assertThat(context.getBean(Greeter.class).greet("Ada")).isEqualTo("Hi Ada");
        });
    }

    @Test
    void switchedOffByAProperty() {
        runner.withPropertyValues("greeting.enabled=false")
            .run(context -> assertThat(context).doesNotHaveBean(Greeter.class));
    }

    @Configuration(proxyBeanMethods = false)
    static class MyGreeterConfig {

        @Bean
        Greeter myGreeter() {
            return name -> "Hi " + name;
        }

    }

}
