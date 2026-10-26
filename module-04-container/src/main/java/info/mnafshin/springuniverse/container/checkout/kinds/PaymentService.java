package info.mnafshin.springuniverse.container.checkout.kinds;

import org.springframework.context.annotation.Proxyable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.context.annotation.ProxyType.INTERFACES;

/** Spring Framework 7: @Proxyable(INTERFACES) asks for an interface-based (JDK) proxy for this one bean. */
@Service
@Proxyable(INTERFACES)
public class PaymentService implements Payments {

    @Override
    @Transactional
    public boolean charge(String order) {
        return true;
    }
}
