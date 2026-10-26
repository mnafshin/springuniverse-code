package info.mnafshin.springuniverse.container.beanlife.nullfield;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/** Fix 1: do the work in @PostConstruct, which runs after every dependency is injected. */
@Component
public class FixedPriceCache {

    @Autowired
    private PriceSource source;

    private final Map<String, Integer> prices = new HashMap<>();

    public FixedPriceCache() {
        System.out.println("constructor: source = " + source);
    }

    @PostConstruct
    void load() {
        System.out.println("@PostConstruct: source = " + source);
        prices.putAll(source.load());
    }

    public Integer price(String item) {
        return prices.get(item);
    }
}
