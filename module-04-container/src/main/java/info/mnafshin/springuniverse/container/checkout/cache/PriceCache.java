package info.mnafshin.springuniverse.container.checkout.cache;

import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/** The fix: the @Cacheable method lives in another bean and is called through its proxy. */
@Service
public class PriceCache {

    private int lookups;

    @Cacheable("prices")
    public int price(String item) {
        lookups++;
        return Map.of("coffee", 3, "cake", 4).get(item);
    }

    public int lookups() {
        return lookups;
    }
}
