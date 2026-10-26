package info.mnafshin.springuniverse.container.proxies.finalmethod;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** A final method can't be overridden by the proxy subclass: it runs on the proxy itself, where "table" was never set. */
@Service
public class BrokenPriceService {

    private final PriceTable table;

    public BrokenPriceService(PriceTable table) {
        this.table = table;
    }

    @Transactional
    public final int price(String item) {
        return table.lookup(item);
    }
}
