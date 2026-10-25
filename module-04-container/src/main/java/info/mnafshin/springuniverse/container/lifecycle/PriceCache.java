package info.mnafshin.springuniverse.container.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

/**
 * One bean with every init and destroy mechanism, so you can see the order.
 * In real code pick one: @PostConstruct and @PreDestroy.
 */
public class PriceCache implements InitializingBean, DisposableBean {

    private final PriceSource source;
    private final Map<String, Integer> prices = new HashMap<>();

    public PriceCache(PriceSource source) {
        this.source = source;
        Events.log("1 constructor (dependencies injected)");
    }

    @PostConstruct
    public void load() {
        prices.putAll(source.load());
        Events.log("3 @PostConstruct load()");
    }

    @Override
    public void afterPropertiesSet() {
        Events.log("4 afterPropertiesSet()");
    }

    /** named as initMethod in LifecycleConfig */
    public void warmUp() {
        Events.log("5 initMethod warmUp()");
    }

    @PreDestroy
    public void flush() {
        Events.log("@PreDestroy flush()");
    }

    @Override
    public void destroy() {
        Events.log("DisposableBean destroy()");
    }

    /** named as destroyMethod in LifecycleConfig */
    public void shutdown() {
        Events.log("destroyMethod shutdown()");
    }

    public Integer price(String item) {
        return prices.get(item);
    }
}
