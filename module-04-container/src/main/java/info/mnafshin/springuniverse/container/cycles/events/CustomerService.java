package info.mnafshin.springuniverse.container.cycles.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final InvoiceService invoices;

    public CustomerService(InvoiceService invoices) {
        this.invoices = invoices;
    }

    public void payInvoice(String customerId) {
        invoices.pay(customerId);
    }

    @EventListener
    void onPaid(InvoicePaid event) {
        System.out.println("customer " + event.customerId() + " marked as paid");
    }
}
