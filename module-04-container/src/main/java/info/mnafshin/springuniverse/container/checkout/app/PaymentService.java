package info.mnafshin.springuniverse.container.checkout.app;

import info.mnafshin.springuniverse.container.checkout.Tx;
import org.springframework.context.annotation.Proxyable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.context.annotation.ProxyType.INTERFACES;

/** A JDK proxy (@Proxyable(INTERFACES)): the checkout asks for Payments, not for PaymentService. */
@Service
@Proxyable(INTERFACES)
public class PaymentService implements Payments {

    @Override
    @Transactional
    public boolean charge(String order, int amount) {
        System.out.println("charge(" + order + ", " + amount + "): transaction active = " + Tx.active());
        return true;
    }
}
