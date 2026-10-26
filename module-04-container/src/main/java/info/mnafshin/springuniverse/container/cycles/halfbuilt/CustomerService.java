package info.mnafshin.springuniverse.container.cycles.halfbuilt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    InvoiceService invoices;

    public int openInvoices(String customerId) {
        return invoices.countFor(customerId);
    }
}
