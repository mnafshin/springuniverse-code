package info.mnafshin.springuniverse.greeting;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * T5.2 Example 5, the problem. This test FAILS on purpose, so it is tagged {@code trap} and only
 * runs with {@code -Ptrap}. The auto-configuration is registered as user configuration, before
 * {@code MyGreeterConfig}, so {@code @ConditionalOnMissingBean} checks too early (the trap from
 * T5.1) and the context ends up with two greeters. {@link GreetingAutoConfigurationTest} shows the fix.
 */
@Tag("trap")
class GreetingWrongSetupTest {

    @Test
    void backsOffForYourOwnGreeter() {
        new ApplicationContextRunner()
            .withUserConfiguration(GreetingAutoConfiguration.class, MyGreeterConfig.class)
            .run(context -> assertThat(context).hasSingleBean(Greeter.class));
    }

    @Configuration(proxyBeanMethods = false)
    static class MyGreeterConfig {

        @Bean
        Greeter myGreeter() {
            return name -> "Hi " + name;
        }

    }

}
