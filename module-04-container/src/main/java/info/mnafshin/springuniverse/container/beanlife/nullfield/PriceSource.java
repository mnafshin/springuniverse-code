package info.mnafshin.springuniverse.container.beanlife.nullfield;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PriceSource {

    public Map<String, Integer> load() {
        return Map.of("coffee", 3, "tea", 2);
    }

    @Override
    public String toString() {
        return "PriceSource";
    }
}
