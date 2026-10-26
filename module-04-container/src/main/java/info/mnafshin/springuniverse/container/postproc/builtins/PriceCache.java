package info.mnafshin.springuniverse.container.postproc.builtins;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public class PriceCache {

    @Autowired
    private PriceSource source;

    @PostConstruct
    void load() {
        System.out.println("@PostConstruct load(): source = " + source);
    }

    PriceSource source() {
        return source;
    }
}
