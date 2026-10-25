package info.mnafshin.springuniverse.container.cyclefix;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerDirectory directory;

    public CustomerService(CustomerDirectory directory) {
        this.directory = directory;
    }

    public String greet(String customerId) {
        return "Hello, " + directory.nameOf(customerId);
    }
}
