package info.mnafshin.springuniverse.container.checkout.cache;

import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/** The same trap with @Cacheable: total() calls price() on "this", so the cache is never used. */
@Service
public class OrderService {

    private int lookups;

    public int total(String... items) {
        int sum = 0;
        for (String item : items) {
            sum += price(item); // this.price(): skips the proxy, so no cache
        }
        return sum;
    }

    @Cacheable("prices")
    public int price(String item) {
        lookups++; // a slow look-up in the price table
        return Map.of("coffee", 3, "cake", 4).get(item);
    }

    public int lookups() {
        return lookups;
    }
}
