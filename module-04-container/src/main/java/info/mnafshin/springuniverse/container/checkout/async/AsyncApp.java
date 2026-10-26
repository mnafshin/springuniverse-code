package info.mnafshin.springuniverse.container.checkout.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AsyncApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(AsyncApp.class, args)) {
            System.out.println("checkout runs on: " + Thread.currentThread().getName());
            System.out.println("-- checkout() calls this.sendConfirmation()");
            System.out.println("confirmation sent on: " + context.getBean(OrderService.class).checkout("A-5").join());
            System.out.println("-- fix: ConfirmationMailer, another bean");
            System.out.println("confirmation sent on: " + context.getBean(FixedOrderService.class).checkout("A-6").join());
        }
    }
}
