package info.mnafshin.springuniverse.customizing.exclude.wrong;

import info.mnafshin.springuniverse.notifier.LogNotifier;
import info.mnafshin.springuniverse.notifier.Notifier;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Example 3, the problem: we don't want the auto-configured notifier, so we exclude... the bean's
 * class. {@code exclude} only takes auto-configuration classes, so the app fails to start.
 */
@SpringBootApplication(exclude = LogNotifier.class)
public class WrongExcludeApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(WrongExcludeApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            System.out.println("notifier beans: " + context.getBeanNamesForType(Notifier.class).length);
        }
    }

}
