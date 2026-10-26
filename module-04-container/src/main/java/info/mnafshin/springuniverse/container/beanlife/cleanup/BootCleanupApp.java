package info.mnafshin.springuniverse.container.beanlife.cleanup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot registers a JVM shutdown hook, so Ctrl+C or a normal kill (SIGTERM) closes the context
 * and runs @PreDestroy. A forced kill (kill -9) skips it.
 */
@SpringBootApplication
public class BootCleanupApp {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(BootCleanupApp.class, args);
        System.out.println("running, stop me with Ctrl+C");
        Thread.currentThread().join();
    }
}
