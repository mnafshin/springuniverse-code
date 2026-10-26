package info.mnafshin.springuniverse.container.shop.callbacks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CallbacksConfig {

    @Bean
    public PriceStore priceStore() {
        return new PriceStore();
    }

    @Bean(initMethod = "warmUp", destroyMethod = "shutdown")
    public PriceCache priceCache() {
        return new PriceCache();
    }
}
