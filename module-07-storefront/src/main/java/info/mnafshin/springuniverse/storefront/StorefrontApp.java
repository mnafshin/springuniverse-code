package info.mnafshin.springuniverse.storefront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.service.registry.ImportHttpServices;

@SpringBootApplication
@ImportHttpServices(group = "shop", types = ProductClient.class)
public class StorefrontApp {

    public static void main(String[] args) {
        SpringApplication.run(StorefrontApp.class, args);
    }
}
