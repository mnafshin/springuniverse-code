package info.mnafshin.springuniverse.container.beanlife.nullfield;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/** Fix 2 (the first choice, see T2.1): constructor injection. The dependency arrives as a parameter. */
@Component
public class ConstructorPriceCache {

    private final Map<String, Integer> prices = new HashMap<>();

    public ConstructorPriceCache(PriceSource source) {
        System.out.println("constructor: source = " + source);
        prices.putAll(source.load());
    }

    public Integer price(String item) {
        return prices.get(item);
    }
}
