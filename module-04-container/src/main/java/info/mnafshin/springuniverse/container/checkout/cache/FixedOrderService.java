package info.mnafshin.springuniverse.container.checkout.cache;

import org.springframework.stereotype.Service;

@Service
public class FixedOrderService {

    private final PriceCache prices;

    public FixedOrderService(PriceCache prices) {
        this.prices = prices;
    }

    public int total(String... items) {
        int sum = 0;
        for (String item : items) {
            sum += prices.price(item); // through the PriceCache proxy
        }
        return sum;
    }
}
