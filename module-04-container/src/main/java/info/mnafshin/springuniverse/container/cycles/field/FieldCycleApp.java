package info.mnafshin.springuniverse.container.cycles.field;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/** The same cycle with field injection. Spring Boot still refuses it unless spring.main.allow-circular-references=true. */
@SpringBootApplication
public class FieldCycleApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(FieldCycleApp.class, args)) {
            CustomerService customers = context.getBean(CustomerService.class);
            InvoiceService invoices = context.getBean(InvoiceService.class);
            System.out.println("started");
            System.out.println("customers.invoices is the invoice service? " + (customers.invoices == invoices));
            System.out.println("invoices.customers is the customer service? " + (invoices.customers == customers));
        }
    }
}
