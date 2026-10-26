package info.mnafshin.springuniverse.container.checkout.selfcall;

import info.mnafshin.springuniverse.container.checkout.OrderTables;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(OrderTables.class)
public class SelfCallApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(SelfCallApp.class, args)) {
            OrderService orders = context.getBean(OrderService.class);
            OrderTables tables = context.getBean(OrderTables.class);
            System.out.println("-- save() from outside: coffee, muffin");
            try {
                orders.save("A-1", "coffee", "muffin");
            } catch (RuntimeException e) {
                System.out.println("failed: " + e.getMessage());
            }
            System.out.println(tables.counts());
            System.out.println("-- checkout() calls save(): coffee, muffin");
            try {
                orders.checkout("A-2", "coffee", "muffin");
            } catch (RuntimeException e) {
                System.out.println("failed: " + e.getMessage());
            }
            System.out.println(tables.counts());
        }
    }
}
