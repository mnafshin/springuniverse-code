package info.mnafshin.springuniverse.container.cycles.field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    CustomerService customers;
}
