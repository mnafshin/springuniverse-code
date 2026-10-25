package info.mnafshin.springuniverse.container.fieldcycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    InvoiceService invoices;

    public InvoiceService invoices() {
        return invoices;
    }
}
