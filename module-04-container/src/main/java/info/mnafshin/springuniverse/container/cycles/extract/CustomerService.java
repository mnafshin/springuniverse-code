package info.mnafshin.springuniverse.container.cycles.extract;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerDirectory directory;
    private final InvoiceService invoices;

    public CustomerService(CustomerDirectory directory, InvoiceService invoices) {
        this.directory = directory;
        this.invoices = invoices;
    }

    public String welcome(String customerId) {
        return "welcome " + directory.nameOf(customerId) + ", " + invoices.invoiceFor(customerId);
    }
}
