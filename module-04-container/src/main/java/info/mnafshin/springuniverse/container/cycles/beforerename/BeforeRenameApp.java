package info.mnafshin.springuniverse.container.cycles.beforerename;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The halfbuilt example before a rename: with circular references allowed, BillingService is found (and created)
 * before CustomerService, so its @PostConstruct gets a finished CustomerService and startup works.
 * Rename BillingService to InvoiceService (package halfbuilt) and CustomerService comes first: startup fails.
 */
@SpringBootApplication
public class BeforeRenameApp {

    public static void main(String[] args) {
        SpringApplication.run(BeforeRenameApp.class, args).close();
        System.out.println("started");
    }
}
