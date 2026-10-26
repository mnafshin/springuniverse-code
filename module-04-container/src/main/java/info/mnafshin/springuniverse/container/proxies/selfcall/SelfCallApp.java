package info.mnafshin.springuniverse.container.proxies.selfcall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SelfCallApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(SelfCallApp.class, args)) {
            OrderService orders = context.getBean(OrderService.class);
            System.out.println("-- save() from outside");
            orders.save("A-1");
            System.out.println("-- checkout() calls save()");
            orders.checkout("A-2");
            System.out.println("-- fix: save() in another bean");
            context.getBean(FixedOrderService.class).checkout("A-3");
        }
    }
}
