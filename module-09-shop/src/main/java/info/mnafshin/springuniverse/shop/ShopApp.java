package info.mnafshin.springuniverse.shop;

import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShopApp {

    public static void main(String[] args) {
        SpringApplication.run(ShopApp.class, args);
    }

    @Bean
    ApplicationRunner seed(ProductRepository repository) {
        return args -> repository.saveAll(List.of(
                new Product(1L, "Coffee"),
                new Product(2L, "Tea")));
    }
}
