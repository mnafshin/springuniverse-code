package info.mnafshin.springuniverse.container.checkout.finalmethod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FinalMethodApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(FinalMethodApp.class, args)) {
            System.out.println("-- final total()");
            try {
                System.out.println("total: " + context.getBean(BrokenOrderService.class).total("coffee", "cake"));
            } catch (RuntimeException e) {
                System.out.println("failed: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            System.out.println("-- normal total()");
            System.out.println("total: " + context.getBean(FixedOrderService.class).total("coffee", "cake"));
        }
    }
}
