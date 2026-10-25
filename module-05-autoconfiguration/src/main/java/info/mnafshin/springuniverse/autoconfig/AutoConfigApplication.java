package info.mnafshin.springuniverse.autoconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * {@code @SpringBootApplication} switches on auto-configuration. Start the jar with
 * {@code --debug} to print the conditions evaluation report.
 */
@SpringBootApplication
public class AutoConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoConfigApplication.class, args);
    }

}
