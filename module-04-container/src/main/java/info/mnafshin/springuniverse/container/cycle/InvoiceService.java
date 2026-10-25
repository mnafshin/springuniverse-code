package info.mnafshin.springuniverse.container.cycle;

import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final CustomerService customers;

    public InvoiceService(CustomerService customers) {
        this.customers = customers;
    }
}
