package info.mnafshin.springuniverse.greeting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * T5.3 cold open, the "before" state: a plain {@code @Configuration} class in a shared library jar.
 * The app's component scan covers only the app's own package ({@code greetingapp}), so this class is
 * never found and the app fails to start. {@code module-05-greeting-starter} is the fix.
 */
@Configuration(proxyBeanMethods = false)
public class GreetingConfig {

    @Bean
    Greeter greeter() {
        return new DefaultGreeter();
    }

}
