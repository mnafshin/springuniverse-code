package info.mnafshin.springuniverse.config.boot;

import info.mnafshin.springuniverse.config.app.AppConfig;
import info.mnafshin.springuniverse.config.app.SignupService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * The same container in Spring Boot. @SpringBootApplication is itself a configuration class
 * (@SpringBootConfiguration is a @Configuration) and scans its own package, ...config.boot.
 * AppConfig lives in another package, so we import it. SpringApplication.run returns the ApplicationContext.
 */
@SpringBootApplication
@Import(AppConfig.class)
public class BootApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(BootApp.class, args)) {
            context.getBean(SignupService.class).register("Ada");
            System.out.println("context: " + context.getClass().getSimpleName());
        }
    }
}
