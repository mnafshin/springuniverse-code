package info.mnafshin.springuniverse.ownapp;

import info.mnafshin.springuniverse.greeting.Greeter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * T5.3: the same app with one more bean, its own {@link Greeter}. The library's default is
 * {@code @ConditionalOnMissingBean}, and auto-configurations are processed after the app's own
 * beans, so the default backs off and the app prints "Hi Ada".
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

    @Bean
    Greeter myGreeter() {
        return name -> "Hi " + name;
    }

}
