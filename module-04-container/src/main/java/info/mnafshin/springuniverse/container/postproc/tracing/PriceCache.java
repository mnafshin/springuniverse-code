package info.mnafshin.springuniverse.container.postproc.tracing;

import jakarta.annotation.PostConstruct;

public class PriceCache {

    @PostConstruct
    void load() {
        System.out.println("  @PostConstruct load()");
    }
}
