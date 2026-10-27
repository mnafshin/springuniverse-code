package info.mnafshin.springuniverse.container.checkout.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class CacheApp {

    /** a simple in-memory cache named "prices" (spring-context has it; no extra dependency) */
    @Bean
    CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("prices");
    }

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(CacheApp.class, args)) {
            System.out.println("-- total() calls this.price(): coffee, coffee, coffee");
            OrderService orders = context.getBean(OrderService.class);
            System.out.println("total: " + orders.total("coffee", "coffee", "coffee"));
            System.out.println("price lookups: " + orders.lookups());
            System.out.println("-- fix: PriceCache, another bean");
            FixedOrderService fixed = context.getBean(FixedOrderService.class);
            System.out.println("total: " + fixed.total("coffee", "coffee", "coffee"));
            System.out.println("price lookups: " + context.getBean(PriceCache.class).lookups());
        }
    }
}
