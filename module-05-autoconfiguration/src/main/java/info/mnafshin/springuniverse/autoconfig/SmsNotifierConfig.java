package info.mnafshin.springuniverse.autoconfig;

import info.mnafshin.springuniverse.notifier.Notifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Our own {@link Notifier}. Because this bean exists, the default from
 * {@code NotifierAutoConfiguration} backs off ({@code @ConditionalOnMissingBean}).
 * It sits behind the {@code sms} profile so you can run the app both ways.
 */
@Configuration(proxyBeanMethods = false)
@Profile("sms")
class SmsNotifierConfig {

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
