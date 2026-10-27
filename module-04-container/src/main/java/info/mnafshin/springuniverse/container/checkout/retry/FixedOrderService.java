package info.mnafshin.springuniverse.container.checkout.retry;

import org.springframework.stereotype.Service;

@Service
public class FixedOrderService {

    private final PaymentClient payments;

    public FixedOrderService(PaymentClient payments) {
        this.payments = payments;
    }

    public String checkout(String id) {
        return payments.charge(id); // through the PaymentClient proxy
    }
}
