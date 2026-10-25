package info.mnafshin.springuniverse.container.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ProxyApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(ProxyApp.class, args)) {
            OrderService orders = context.getBean(OrderService.class);
            Payments payments = context.getBean(Payments.class);
            System.out.println("orderService   -> " + orders.getClass().getName());
            System.out.println("paymentService -> " + payments.getClass().getName());
            System.out.println("save() from outside:     transaction active = " + orders.save("A-1"));
            System.out.println("checkout() calls save(): transaction active = " + orders.checkout("A-2"));
            System.out.println("fixed checkout():        transaction active = "
                    + context.getBean(FixedOrderService.class).checkout("A-3"));
            System.out.println("charge() via JDK proxy:  transaction active = " + payments.charge("A-4"));
        }
    }
}
