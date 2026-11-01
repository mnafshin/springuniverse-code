package info.mnafshin.springuniverse.prices;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriceConfiguration {

    @Bean
    PriceFormatter priceFormatter() {
        return new PriceFormatter("USD");
    }

}
