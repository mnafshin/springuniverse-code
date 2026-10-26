package info.mnafshin.springuniverse.greetingapp;

import info.mnafshin.springuniverse.greeting.Greeter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * T5.2 Example 4: this app only depends on {@code module-05-greeting-starter}. It never defines a
 * {@link Greeter}, and its component scan ({@code greetingapp}) never reaches the starter's package
 * ({@code greeting}). The greeter comes from the starter's {@code AutoConfiguration.imports} file.
 */
@SpringBootApplication
public class GreetingApp {

    public static void main(String[] args) {
        SpringApplication.run(GreetingApp.class, args).close();
    }

    @Bean
    CommandLineRunner greet(Greeter greeter) {
        return args -> System.out.println(greeter.greet("Ada"));
    }

}
