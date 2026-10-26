package info.mnafshin.springuniverse.container.cycles.halfbuilt;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    CustomerService customers;

    @PostConstruct
    void warmUp() {
        System.out.println("InvoiceService @PostConstruct: asking the customer service");
        System.out.println("open invoices: " + customers.openInvoices("c-1"));
    }

    public int countFor(String customerId) {
        return 2;
    }
}
