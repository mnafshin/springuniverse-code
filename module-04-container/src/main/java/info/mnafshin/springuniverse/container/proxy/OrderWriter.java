package info.mnafshin.springuniverse.container.proxy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The fix: the transactional method lives in another bean. */
@Service
public class OrderWriter {

    @Transactional
    public boolean save(String order) {
        return Tx.active();
    }
}
