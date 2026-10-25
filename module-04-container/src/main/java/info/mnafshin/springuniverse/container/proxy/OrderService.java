package info.mnafshin.springuniverse.container.proxy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The self-invocation trap: checkout() calls save() on "this", not on the proxy. */
@Service
public class OrderService {

    public boolean checkout(String order) {
        return save(order); // this.save(...): skips the proxy, so no transaction
    }

    @Transactional
    public boolean save(String order) {
        return Tx.active();
    }
}
