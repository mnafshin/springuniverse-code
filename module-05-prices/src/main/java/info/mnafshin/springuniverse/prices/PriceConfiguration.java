package info.mnafshin.springuniverse.prices;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class PriceConfiguration {

    @Bean
    PriceFormatter priceFormatter() {
        return new PriceFormatter("USD");
    }

}
