package info.mnafshin.springuniverse.container.cycles.extract;

import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final CustomerDirectory directory;

    public InvoiceService(CustomerDirectory directory) {
        this.directory = directory;
    }

    public String invoiceFor(String customerId) {
        return "invoice for " + directory.nameOf(customerId);
    }
}
