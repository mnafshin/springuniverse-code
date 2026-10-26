package info.mnafshin.springuniverse.container.proxies.finalmethod;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The fix: not final, so the proxy overrides price() and passes the call to the real object. */
@Service
public class FixedPriceService {

    private final PriceTable table;

    public FixedPriceService(PriceTable table) {
        this.table = table;
    }

    @Transactional
    public int price(String item) {
        return table.lookup(item);
    }
}
