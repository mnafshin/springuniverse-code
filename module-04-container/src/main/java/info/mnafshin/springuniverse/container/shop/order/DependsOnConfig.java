package info.mnafshin.springuniverse.container.shop.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/** The fix: @DependsOn names the hidden dependency, so Migrations runs first. */
@Configuration
public class DependsOnConfig {

    @Bean
    public PriceCache priceCache(PriceSource priceSource) {
        return new PriceCache(priceSource);
    }

    @Bean
    @DependsOn("migrations")
    public PriceSource priceSource() {
        return new PriceSource();
    }

    @Bean
    public Migrations migrations() {
        return new Migrations();
    }
}
