package info.mnafshin.springuniverse.container.postproc.wrapper;

import java.util.Map;

public class SimplePriceService implements PriceService {

    private final Map<String, Integer> prices = Map.of("coffee", 3, "tea", 2);

    @Override
    public int price(String item) {
        return prices.get(item);
    }
}
