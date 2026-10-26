package info.mnafshin.springuniverse.container.checkout.fix;

import info.mnafshin.springuniverse.container.checkout.OrderTables;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(OrderTables.class)
public class FixApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(FixApp.class, args)) {
            OrderService orders = context.getBean(OrderService.class);
            OrderTables tables = context.getBean(OrderTables.class);
            System.out.println("-- checkout() calls writer.save(): coffee, muffin");
            try {
                orders.checkout("A-3", "coffee", "muffin");
            } catch (RuntimeException e) {
                System.out.println("failed: " + e.getMessage());
            }
            System.out.println(tables.counts());
            System.out.println("-- checkout(): coffee, cake");
            orders.checkout("A-4", "coffee", "cake");
            System.out.println(tables.counts());
        }
    }
}
