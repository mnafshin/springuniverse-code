package info.mnafshin.springuniverse.container.shop.app;

import jakarta.annotation.PreDestroy;

import java.util.Map;

/** Reads the prices table, so Migrations must run first. */
public class PriceSource {

    public PriceSource() {
        System.out.println("PriceSource: table prices " + (Database.hasTable("prices") ? "found" : "is missing!"));
    }

    public Map<String, Integer> load() {
        return Map.of("coffee", 3, "tea", 2);
    }

    @PreDestroy
    public void close() {
        System.out.println("PriceSource closed");
    }
}
