package info.mnafshin.springuniverse.container.checkout.kinds;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Has an interface, but Spring Boot still makes a class-based (CGLIB) proxy: a subclass of ShippingService. */
@Service
public class ShippingService implements Shipping {

    @Override
    @Transactional
    public String ship(String order) {
        return order + " shipped";
    }
}
