package info.mnafshin.springuniverse.container.checkout.finalmethod;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The fix: not final, so the proxy overrides total() and passes the call to the real object. */
@Service
public class FixedOrderService {

    private final PriceTable prices;

    public FixedOrderService(PriceTable prices) {
        this.prices = prices;
    }

    @Transactional(readOnly = true)
    public int total(String... items) {
        int sum = 0;
        for (String item : items) {
            sum += prices.price(item);
        }
        return sum;
    }
}
