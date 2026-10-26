package info.mnafshin.springuniverse.decides.trap;

import info.mnafshin.springuniverse.notifier.Notifier;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Example 4 (the problem): {@code @ConditionalOnMissingBean} in an ordinary {@code @Configuration}
 * of the app. {@link FallbackConfig} is processed before {@link SmsConfig}, so when its condition
 * is checked, {@code smsNotifier} isn't registered yet: we end up with two notifiers.
 */
@SpringBootApplication
public class TrapApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(TrapApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            System.out.println("notifier beans: " + context.getBeansOfType(Notifier.class).keySet());
            Notifier notifier = context.getBean(Notifier.class);
            System.out.println("notifier: " + notifier.name());
        }
    }

}
