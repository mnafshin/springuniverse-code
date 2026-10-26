package info.mnafshin.springuniverse.container.proxies.selfcall;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The fix: the transactional method lives in another bean. */
@Service
public class OrderWriter {

    @Transactional
    public void save(String order) {
        System.out.println("save(" + order + "): transaction active = " + Tx.active());
    }
}
