package info.mnafshin.springuniverse.container.lifecycle;

import jakarta.annotation.PreDestroy;

import java.util.Map;

/** A dependency of PriceCache. It is created first and destroyed last. */
public class PriceSource {

    public PriceSource() {
        Events.log("PriceSource created");
    }

    public Map<String, Integer> load() {
        return Map.of("coffee", 3, "tea", 2);
    }

    @PreDestroy
    public void close() {
        Events.log("PriceSource closed");
    }
}
