package info.mnafshin.springuniverse.container.shop.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;

/** The whole shop app, with every fix from the video. */
@Configuration
public class ShopConfig {

    @Bean
    public Migrations migrations() {
        return new Migrations();
    }

    @Bean
    @DependsOn("migrations")
    public PriceSource priceSource() {
        return new PriceSource();
    }

    @Bean
    public PriceCache priceCache(PriceSource source) {
        return new PriceCache(source);
    }

    @Bean
    @Lazy
    public SalesReport salesReport() {
        return new SalesReport();
    }

    @Bean
    public PriceExport priceExport() {
        return new PriceExport();
    }
}
