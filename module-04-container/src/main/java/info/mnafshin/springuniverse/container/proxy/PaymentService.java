package info.mnafshin.springuniverse.container.proxy;

import org.springframework.context.annotation.Proxyable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.context.annotation.ProxyType.INTERFACES;

/**
 * Spring Boot uses CGLIB (class-based) proxies by default.
 * Spring Framework 7's @Proxyable(INTERFACES) asks for a JDK dynamic proxy for this one bean.
 * Inject it as Payments: the proxy implements the interface, it is not a PaymentService.
 */
@Service
@Proxyable(INTERFACES)
public class PaymentService implements Payments {

    @Override
    @Transactional
    public boolean charge(String order) {
        return Tx.active();
    }
}
