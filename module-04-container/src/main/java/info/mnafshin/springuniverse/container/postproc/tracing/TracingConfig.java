package info.mnafshin.springuniverse.container.postproc.tracing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    @Bean
    public static TracingPostProcessor tracingPostProcessor() {
        return new TracingPostProcessor();
    }

    @Bean
    public Menu menu() {
        return new Menu();
    }

    @Bean
    public PriceCache priceCache() {
        return new PriceCache();
    }
}
