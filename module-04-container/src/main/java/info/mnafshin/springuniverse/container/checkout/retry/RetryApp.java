package info.mnafshin.springuniverse.container.checkout.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.resilience.annotation.EnableResilientMethods;

/** @Retryable is built into Spring Framework 7 (org.springframework.resilience), no extra dependency. */
@SpringBootApplication
@EnableResilientMethods
public class RetryApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(RetryApp.class, args)) {
            System.out.println("-- checkout() calls this.charge(): A-6");
            try {
                System.out.println("result: " + context.getBean(OrderService.class).checkout("A-6"));
            } catch (RuntimeException e) {
                System.out.println("failed: " + e.getMessage());
            }
            System.out.println("-- fix: PaymentClient, another bean");
            System.out.println("result: " + context.getBean(FixedOrderService.class).checkout("A-7"));
        }
    }
}
