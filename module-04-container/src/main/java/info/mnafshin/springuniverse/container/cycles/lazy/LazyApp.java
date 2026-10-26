package info.mnafshin.springuniverse.container.cycles.lazy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/** A last resort: @Lazy on one injection point gives InvoiceService a proxy, so the cycle starts. */
@SpringBootApplication
public class LazyApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(LazyApp.class, args)) {
            InvoiceService invoices = context.getBean(InvoiceService.class);
            System.out.println("started");
            System.out.println("injected: " + invoices.customers().getClass().getSimpleName());
            System.out.println(invoices.invoiceFor("c-1"));
        }
    }
}
