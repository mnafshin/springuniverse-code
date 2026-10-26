package info.mnafshin.springuniverse.container.shop.order;

import jakarta.annotation.PreDestroy;

/** Needs PriceSource in its constructor: a direct dependency. */
public class PriceCache {

    public PriceCache(PriceSource source) {
        System.out.println("PriceCache created");
    }

    @PreDestroy
    public void close() {
        System.out.println("PriceCache closed");
    }
}
