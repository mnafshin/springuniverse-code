package info.mnafshin.springuniverse.decides.trapfix;

import info.mnafshin.springuniverse.decides.SmsNotifier;
import info.mnafshin.springuniverse.notifier.Notifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** The app's real notifier; the fallback now comes from {@code NotifierAutoConfiguration}. */
@Configuration(proxyBeanMethods = false)
class SmsConfig {

    @Bean
    Notifier smsNotifier() {
        return new SmsNotifier();
    }

}
