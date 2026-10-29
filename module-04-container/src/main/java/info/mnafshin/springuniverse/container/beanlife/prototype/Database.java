package info.mnafshin.springuniverse.container.beanlife.prototype;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

/** A normal singleton: Spring calls its @PreDestroy when the container closes. */
@Component
public class Database {

    @PreDestroy
    void shutdown() {
        System.out.println("Database closed");
    }
}
