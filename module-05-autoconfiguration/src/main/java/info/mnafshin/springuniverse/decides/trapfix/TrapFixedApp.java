package info.mnafshin.springuniverse.decides.trapfix;

import info.mnafshin.springuniverse.notifier.Notifier;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Example 4 (the fix): no fallback in the app's own configuration. The fallback lives in an
 * auto-configuration ({@code NotifierAutoConfiguration}), which is processed after all of the app's
 * beans are registered, so its {@code @ConditionalOnMissingBean} sees {@code smsNotifier}.
 */
@SpringBootApplication
public class TrapFixedApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(TrapFixedApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            System.out.println("notifier beans: " + context.getBeansOfType(Notifier.class).keySet());
            Notifier notifier = context.getBean(Notifier.class);
            System.out.println("notifier: " + notifier.name());
        }
    }

}
