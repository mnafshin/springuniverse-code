package info.mnafshin.springuniverse.customizing.exclude.right;

import info.mnafshin.springuniverse.notifier.Notifier;
import info.mnafshin.springuniverse.notifier.NotifierAutoConfiguration;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Example 3, the fix: exclude the auto-configuration class itself. Without touching code, the same
 * works with {@code spring.autoconfigure.exclude} (T5.1, "Reading the report").
 */
@SpringBootApplication(exclude = NotifierAutoConfiguration.class)
public class ExcludeApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(ExcludeApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            System.out.println("notifier beans: " + context.getBeanNamesForType(Notifier.class).length);
        }
    }

}
