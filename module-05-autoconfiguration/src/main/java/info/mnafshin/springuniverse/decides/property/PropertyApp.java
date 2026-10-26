package info.mnafshin.springuniverse.decides.property;

import info.mnafshin.springuniverse.notifier.Notifier;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Example 5: one property switches the whole {@code NotifierAutoConfiguration} on or off
 * ({@code @ConditionalOnBooleanProperty(name = "notifier.enabled", matchIfMissing = true)}).
 * Try {@code --notifier.enable=false} (a typo: nothing changes) and {@code --notifier.enabled=false}.
 */
@SpringBootApplication
public class PropertyApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(PropertyApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            System.out.println("notifier beans: " + context.getBeansOfType(Notifier.class).size());
        }
    }

}
