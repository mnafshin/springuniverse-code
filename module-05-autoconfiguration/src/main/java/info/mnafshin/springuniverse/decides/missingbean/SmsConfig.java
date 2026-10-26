package info.mnafshin.springuniverse.decides.missingbean;

import info.mnafshin.springuniverse.decides.SmsNotifier;
import info.mnafshin.springuniverse.notifier.Notifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** Our own {@link Notifier}, only with the {@code sms} profile. */
@Configuration(proxyBeanMethods = false)
@Profile("sms")
class SmsConfig {

    @Bean
    Notifier smsNotifier() {
        return new SmsNotifier();
    }

}
