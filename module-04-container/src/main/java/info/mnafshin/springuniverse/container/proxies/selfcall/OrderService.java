package info.mnafshin.springuniverse.container.proxies.selfcall;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The self-call trap: checkout() calls save() on "this", not on the proxy. */
@Service
public class OrderService {

    public void checkout(String order) {
        save(order); // this.save(...): skips the proxy, so no transaction
    }

    @Transactional
    public void save(String order) {
        System.out.println("save(" + order + "): transaction active = " + Tx.active());
    }
}
