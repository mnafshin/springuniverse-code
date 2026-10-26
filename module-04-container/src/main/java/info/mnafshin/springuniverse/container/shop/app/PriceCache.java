package info.mnafshin.springuniverse.container.shop.app;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.HashMap;
import java.util.Map;

/** Fix 1: the source arrives through the constructor, and the setup work runs in @PostConstruct. */
public class PriceCache {

    private final PriceSource source;
    private final Map<String, Integer> prices = new HashMap<>();

    public PriceCache(PriceSource source) {
        this.source = source;
    }

    @PostConstruct
    void load() {
        prices.putAll(source.load());
        System.out.println("PriceCache: " + prices.size() + " prices loaded");
    }

    public Integer price(String item) {
        return prices.get(item);
    }

    @PreDestroy
    public void close() {
        System.out.println("PriceCache closed");
    }
}
