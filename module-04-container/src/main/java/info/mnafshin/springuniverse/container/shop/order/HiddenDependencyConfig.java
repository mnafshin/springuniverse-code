package info.mnafshin.springuniverse.container.shop.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** The problem: PriceSource needs Migrations, but nothing tells Spring. */
@Configuration
public class HiddenDependencyConfig {

    @Bean
    public PriceCache priceCache(PriceSource priceSource) {
        return new PriceCache(priceSource);
    }

    @Bean
    public PriceSource priceSource() {
        return new PriceSource();
    }

    @Bean
    public Migrations migrations() {
        return new Migrations();
    }
}
