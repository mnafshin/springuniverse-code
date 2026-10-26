package info.mnafshin.springuniverse.container.proxies.finalmethod;

import org.springframework.stereotype.Component;

@Component
public class PriceTable {

    public int lookup(String item) {
        return 3;
    }
}
