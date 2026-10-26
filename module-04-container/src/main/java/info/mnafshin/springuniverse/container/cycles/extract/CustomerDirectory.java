package info.mnafshin.springuniverse.container.cycles.extract;

import org.springframework.stereotype.Component;

/** The part InvoiceService needed from CustomerService, moved into its own bean. */
@Component
public class CustomerDirectory {

    public String nameOf(String customerId) {
        return "Ada";
    }
}
