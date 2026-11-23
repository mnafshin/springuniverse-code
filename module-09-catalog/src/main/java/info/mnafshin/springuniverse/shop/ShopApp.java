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
    ApplicationRunner seed(CategoryRepository categories, ProductRepository products) {
        return args -> {
            Category drinks = new Category(1L, "Drinks");
            Category bakery = new Category(2L, "Bakery");
            Category beans = new Category(3L, "Beans");
            categories.saveAll(List.of(drinks, bakery, beans));
            products.saveAll(List.of(
                    new Product(1L, "Coffee", drinks),
                    new Product(2L, "Tea", drinks),
                    new Product(3L, "Croissant", bakery),
                    new Product(4L, "Espresso beans", beans)));
        };
    }
}
