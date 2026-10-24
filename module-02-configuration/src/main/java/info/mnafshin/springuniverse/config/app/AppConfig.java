package info.mnafshin.springuniverse.config.app;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The main configuration class:
 * - @ComponentScan with no package: scans this class's package (and below), so it finds EmailNotifier and SignupService.
 * - @Import pulls in a second configuration class.
 * - @Bean for a class we don't own: java.time.Clock.
 */
@Configuration
@ComponentScan
@Import(PersistenceConfig.class)
public class AppConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
