package info.mnafshin.springuniverse.container.cycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Two services that need each other through their constructors: this app refuses to start. */
@SpringBootApplication
public class CycleApp {

    public static void main(String[] args) {
        SpringApplication.run(CycleApp.class, args);
    }
}
