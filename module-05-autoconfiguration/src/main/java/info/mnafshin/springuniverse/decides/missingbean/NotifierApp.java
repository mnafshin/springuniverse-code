package info.mnafshin.springuniverse.decides.missingbean;

import info.mnafshin.springuniverse.notifier.Notifier;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Example 3: run it plain and the notifier is the auto-configured default ({@code log}); run it
 * with {@code --spring.profiles.active=sms} and our own bean exists, so the default backs off
 * ({@code @ConditionalOnMissingBean}).
 */
@SpringBootApplication
public class NotifierApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(NotifierApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            Notifier notifier = context.getBean(Notifier.class);
            System.out.println("notifier: " + notifier.name() + " (" + notifier.getClass().getSimpleName() + ")");
            System.out.println(notifier.send("Ada", "Welcome, Ada!"));
        }
    }

}
