package info.mnafshin.springuniverse.container.checkout.finalmethod;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** A final method can't be overridden by the proxy subclass: it runs on the proxy itself, where "prices" was never set. */
@Service
public class BrokenOrderService {

    private final PriceTable prices;

    public BrokenOrderService(PriceTable prices) {
        this.prices = prices;
    }

    @Transactional(readOnly = true)
    public final int total(String... items) {
        int sum = 0;
        for (String item : items) {
            sum += prices.price(item);
        }
        return sum;
    }
}
