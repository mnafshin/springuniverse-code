package info.mnafshin.springuniverse.container.proxies.kinds;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Has an interface, but Spring Boot still makes a class-based (CGLIB) proxy: a subclass of RefundService. */
@Service
public class RefundService implements Refunds {

    @Override
    @Transactional
    public boolean refund(String order) {
        return true;
    }
}
