package info.mnafshin.springuniverse.container.proxies.finalmethod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FinalMethodApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(FinalMethodApp.class, args)) {
            System.out.println("-- final method");
            try {
                System.out.println("coffee costs " + context.getBean(BrokenPriceService.class).price("coffee"));
            } catch (RuntimeException e) {
                System.out.println("failed: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            System.out.println("-- normal method");
            System.out.println("coffee costs " + context.getBean(FixedPriceService.class).price("coffee"));
        }
    }
}
