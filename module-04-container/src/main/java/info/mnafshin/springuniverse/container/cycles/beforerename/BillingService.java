package info.mnafshin.springuniverse.container.cycles.beforerename;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Same code as halfbuilt.InvoiceService, only the class name differs. */
@Service
public class BillingService {

    @Autowired
    CustomerService customers;

    @PostConstruct
    void warmUp() {
        System.out.println("BillingService @PostConstruct: asking the customer service");
        System.out.println("open invoices: " + customers.openInvoices("c-1"));
    }

    public int countFor(String customerId) {
        return 2;
    }
}
