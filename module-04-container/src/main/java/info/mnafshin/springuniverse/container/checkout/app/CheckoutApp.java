package info.mnafshin.springuniverse.container.checkout.app;

import info.mnafshin.springuniverse.container.checkout.OrderTables;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(OrderTables.class)
public class CheckoutApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(CheckoutApp.class, args)) {
            CheckoutService checkout = context.getBean(CheckoutService.class);
            OrderTables tables = context.getBean(OrderTables.class);
            System.out.println("-- checkout A-7: coffee, cake");
            checkout.checkout("A-7", "coffee", "cake");
            System.out.println(tables.counts());
            System.out.println("-- checkout A-8: coffee, muffin");
            try {
                checkout.checkout("A-8", "coffee", "muffin");
            } catch (RuntimeException e) {
                System.out.println("failed: " + e.getMessage());
            }
            System.out.println(tables.counts());
        }
    }
}
