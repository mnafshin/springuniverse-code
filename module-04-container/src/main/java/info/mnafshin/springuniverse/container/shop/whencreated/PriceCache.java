package info.mnafshin.springuniverse.container.shop.whencreated;

import org.springframework.stereotype.Component;

/** A normal singleton: created while the container starts. */
@Component
public class PriceCache {

    public PriceCache() {
        System.out.println("PriceCache created");
    }
}
