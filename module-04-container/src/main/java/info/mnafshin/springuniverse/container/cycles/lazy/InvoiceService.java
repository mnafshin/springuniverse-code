package info.mnafshin.springuniverse.container.cycles.lazy;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final CustomerService customers;

    public InvoiceService(@Lazy CustomerService customers) {   // last resort
        this.customers = customers;
    }

    public CustomerService customers() {
        return customers;
    }

    public String invoiceFor(String customerId) {
        return "invoice for " + customers.nameOf(customerId);
    }
}
