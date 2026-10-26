package info.mnafshin.springuniverse.container.postproc.wrapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WrapperConfig {

    @Bean
    public static LoggingPostProcessor loggingPostProcessor() {
        return new LoggingPostProcessor();
    }

    @Bean
    public SimplePriceService priceService() {
        return new SimplePriceService();
    }
}
