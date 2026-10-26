package info.mnafshin.springuniverse.greeting;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Our own auto-configuration, shipped in its own jar.
 *
 * <ul>
 * <li>{@code @AutoConfiguration} plus one line in
 * {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports}
 * makes it a candidate. Component scanning never sees it: it is not in the app's package.</li>
 * <li>{@code greeting.enabled=false} switches it off (missing means on).</li>
 * <li>The default greeter backs off when the app defines its own {@link Greeter}.</li>
 * </ul>
 */
@AutoConfiguration
@ConditionalOnBooleanProperty(name = "greeting.enabled", matchIfMissing = true)
public final class GreetingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    Greeter greeter() {
        return new DefaultGreeter();
    }

}
