package info.mnafshin.springuniverse.container.fieldcycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** The same cycle with field injection. Plain Spring Framework resolves it; Spring Boot still refuses by default. */
@Service
public class InvoiceService {

    @Autowired
    CustomerService customers;

    public CustomerService customers() {
        return customers;
    }
}
