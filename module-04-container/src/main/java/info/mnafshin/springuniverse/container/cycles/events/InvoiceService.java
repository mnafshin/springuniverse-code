package info.mnafshin.springuniverse.container.cycles.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/** Doesn't know CustomerService: it only announces that an invoice was paid. */
@Service
public class InvoiceService {

    private final ApplicationEventPublisher events;

    public InvoiceService(ApplicationEventPublisher events) {
        this.events = events;
    }

    public void pay(String customerId) {
        System.out.println("invoice paid by " + customerId);
        events.publishEvent(new InvoicePaid(customerId));
    }
}
