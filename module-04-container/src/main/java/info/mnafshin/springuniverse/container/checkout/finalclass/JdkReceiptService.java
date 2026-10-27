package info.mnafshin.springuniverse.container.checkout.finalclass;

import info.mnafshin.springuniverse.container.checkout.Tx;
import org.springframework.context.annotation.Proxyable;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.context.annotation.ProxyType.INTERFACES;

/** Still final, but @Proxyable(INTERFACES) asks for a JDK proxy, which only implements Receipts: no subclass needed. */
@Proxyable(INTERFACES)
public final class JdkReceiptService implements Receipts {

    @Override
    @Transactional
    public String print(String id) {
        return "receipt " + id + ", transaction active = " + Tx.active();
    }
}
