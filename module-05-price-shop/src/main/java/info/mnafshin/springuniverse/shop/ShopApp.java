package info.mnafshin.springuniverse.shop;

import info.mnafshin.springuniverse.prices.PriceFormatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShopApp {

    public static void main(String[] args) {
        SpringApplication.run(ShopApp.class, args);
    }

    @Bean
    PriceFormatter euroPrices() {
        return new PriceFormatter("EUR");
    }

}
