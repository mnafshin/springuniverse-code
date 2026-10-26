package info.mnafshin.springuniverse.container.cycles.halfbuilt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Why allow-circular-references is a last resort: InvoiceService gets an early reference to a CustomerService
 * whose fields are not injected yet, and uses it in @PostConstruct.
 */
@SpringBootApplication
public class HalfBuiltApp {

    public static void main(String[] args) {
        SpringApplication.run(HalfBuiltApp.class, args).close();
        System.out.println("started");
    }
}
