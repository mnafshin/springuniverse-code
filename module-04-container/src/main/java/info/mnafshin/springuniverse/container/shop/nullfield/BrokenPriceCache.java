package info.mnafshin.springuniverse.container.shop.nullfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/** The problem: the constructor uses a field that Spring has not injected yet. */
@Component
public class BrokenPriceCache {

    @Autowired
    private PriceSource source;

    private final Map<String, Integer> prices = new HashMap<>();

    public BrokenPriceCache() {
        System.out.println("constructor: source = " + source);
        prices.putAll(source.load());   // source is still null here
    }
}
