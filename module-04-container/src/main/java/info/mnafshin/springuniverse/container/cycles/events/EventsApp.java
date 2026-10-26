package info.mnafshin.springuniverse.container.cycles.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/** The other fix: InvoiceService publishes an event instead of calling CustomerService back. */
@SpringBootApplication
public class EventsApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(EventsApp.class, args)) {
            System.out.println("started");
            context.getBean(CustomerService.class).payInvoice("c-1");
        }
    }
}
