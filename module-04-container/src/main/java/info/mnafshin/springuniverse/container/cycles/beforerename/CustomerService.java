package info.mnafshin.springuniverse.container.cycles.beforerename;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    BillingService invoices;

    public int openInvoices(String customerId) {
        return invoices.countFor(customerId);
    }
}
