package info.mnafshin.springuniverse.decides.trap;

import info.mnafshin.springuniverse.notifier.LogNotifier;
import info.mnafshin.springuniverse.notifier.Notifier;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** The trap: a "fallback" bean with {@code @ConditionalOnMissingBean} in the app's own configuration. */
@Configuration(proxyBeanMethods = false)
class FallbackConfig {

    @Bean
    @ConditionalOnMissingBean
    Notifier fallbackNotifier() {
        return new LogNotifier();
    }

}
