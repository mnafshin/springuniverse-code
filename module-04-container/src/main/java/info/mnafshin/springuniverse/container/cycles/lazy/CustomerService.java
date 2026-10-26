package info.mnafshin.springuniverse.container.cycles.lazy;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final InvoiceService invoices;

    public CustomerService(InvoiceService invoices) {
        this.invoices = invoices;
    }

    public String nameOf(String customerId) {
        return "Ada";
    }
}
