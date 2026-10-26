package info.mnafshin.springuniverse.container.beanlife.callbacks;

/** No annotation, no interface: for a @Bean method, Spring calls a public close() on its own. */
public class PriceStore {

    public void close() {
        System.out.println("PriceStore close(), found by Spring");
    }
}
