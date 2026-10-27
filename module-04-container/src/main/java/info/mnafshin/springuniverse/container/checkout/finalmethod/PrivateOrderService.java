package info.mnafshin.springuniverse.container.checkout.finalmethod;

import info.mnafshin.springuniverse.container.checkout.Tx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** A private method can't be overridden by the proxy, and only this class can call it: always a self-call. */
@Service
public class PrivateOrderService {

    public String checkout(String id) {
        return save(id);
    }

    @Transactional
    private String save(String id) {
        return "save(" + id + "): transaction active = " + Tx.active();
    }
}
