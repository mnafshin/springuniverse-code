package info.mnafshin.springuniverse.container.cycles.extract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/** The fix: CustomerService -> InvoiceService -> CustomerDirectory, all arrows point one way. */
@SpringBootApplication
public class ExtractApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(ExtractApp.class, args)) {
            System.out.println("started");
            System.out.println(context.getBean(CustomerService.class).welcome("c-1"));
        }
    }
}
