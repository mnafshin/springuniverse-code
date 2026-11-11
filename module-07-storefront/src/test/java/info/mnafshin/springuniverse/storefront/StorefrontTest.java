package info.mnafshin.springuniverse.storefront;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.web.servlet.client.RestTestClient;

import info.mnafshin.springuniverse.shop.ShopApp;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureRestTestClient
class StorefrontTest {

    private static ConfigurableApplicationContext shop;

    @Autowired
    private RestTestClient client;

    @BeforeAll
    static void startTheShop() {
        shop = SpringApplication.run(ShopApp.class, "--server.port=8081");
    }

    @AfterAll
    static void stopTheShop() {
        shop.close();
    }

    @Test
    void catalogFetchesTheListFromTheShop() {
        this.client.get().uri("/catalog")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].name").isEqualTo("Coffee")
                .jsonPath("$[1].name").isEqualTo("Tea");
    }

}
