package info.mnafshin.springuniverse.container.checkout.finalmethod;

import java.util.Map;

import org.springframework.stereotype.Component;

/** The shop's prices. A muffin has no price yet, so ordering one fails. */
@Component
public class PriceTable {

    private final Map<String, Integer> prices = Map.of("coffee", 3, "cake", 4);

    public int price(String item) {
        Integer price = prices.get(item);
        if (price == null) {
            throw new IllegalArgumentException("no price for " + item);
        }
        return price;
    }
}
