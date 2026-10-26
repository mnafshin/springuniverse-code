package info.mnafshin.springuniverse.container.shop.callbacks;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * All three init and all three destroy mechanisms on one bean, only to see the order.
 * In real code pick one: @PostConstruct and @PreDestroy.
 */
public class PriceCache implements InitializingBean, DisposableBean {

    public PriceCache() {
        System.out.println("constructor");
    }

    @PostConstruct
    public void load() {
        System.out.println("1 @PostConstruct load()");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("2 afterPropertiesSet()");
    }

    /** named as initMethod in CallbacksConfig */
    public void warmUp() {
        System.out.println("3 initMethod warmUp()");
    }

    @PreDestroy
    public void flush() {
        System.out.println("1 @PreDestroy flush()");
    }

    @Override
    public void destroy() {
        System.out.println("2 destroy()");
    }

    /** named as destroyMethod in CallbacksConfig */
    public void shutdown() {
        System.out.println("3 destroyMethod shutdown()");
    }
}
