package info.mnafshin.springuniverse.container.cycles.ctor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Two services that need each other through their constructors: Spring Boot refuses to start. */
@SpringBootApplication
public class CtorCycleApp {

    public static void main(String[] args) {
        SpringApplication.run(CtorCycleApp.class, args).close();
        System.out.println("started");
    }
}
