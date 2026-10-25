package info.mnafshin.springuniverse.notifier;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * A small auto-configuration, written the way Spring Boot writes its own.
 *
 * <ul>
 * <li>It is listed in {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports},
 * so Boot treats it as a candidate. It lives outside the application's package, so component
 * scanning never picks it up.</li>
 * <li>{@code notifier.enabled=false} switches the whole class off (missing means on).</li>
 * <li>The default {@link LogNotifier} is only created if the app has no {@link Notifier} bean
 * of its own: define one and this default backs off.</li>
 * </ul>
 *
 * In a real project this class would ship in its own jar (a "starter"); it sits in the same
 * module here to keep the example small.
 */
@AutoConfiguration
@ConditionalOnBooleanProperty(name = "notifier.enabled", matchIfMissing = true)
public final class NotifierAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    Notifier notifier() {
        return new LogNotifier();
    }

}
