package info.mnafshin.springuniverse.container.checkout.finalclass;

import info.mnafshin.springuniverse.container.checkout.Tx;
import org.springframework.transaction.annotation.Transactional;

/** A final class: CGLIB can't make a subclass of it, so with Spring Boot's default the app fails to start. */
public final class ReceiptService implements Receipts {

    @Override
    @Transactional
    public String print(String id) {
        return "receipt " + id + ", transaction active = " + Tx.active();
    }
}
