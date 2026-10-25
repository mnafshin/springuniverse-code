package info.mnafshin.springuniverse.container.cycle;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final InvoiceService invoices;

    public CustomerService(InvoiceService invoices) {
        this.invoices = invoices;
    }
}
