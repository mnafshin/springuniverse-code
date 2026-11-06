package info.mnafshin.springuniverse.products;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Hits the embedded server so error bodies match curl. MockMvc does not dispatch
 * to Boot's /error page, which is where the classic JSON used to be written.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class ProductErrorHttpTest {

    @Autowired
    private RestTestClient client;

    @Autowired
    private ApplicationContext context;

    @Test
    void extendingTheHandlerMakesBootBackOff() {
        assertThat(this.context.getEnvironment().getProperty("spring.mvc.problemdetails.enabled")).isEqualTo("true");
        assertThat(this.context.containsBean("problemDetailsExceptionHandler")).isFalse();
        assertThat(this.context.getBeanNamesForType(ResponseEntityExceptionHandler.class))
                .containsExactly("productExceptionHandler");
    }

    @Test
    void missingProductNamesTheId() {
        this.client.get().uri("/products/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Product 99 was not found")
                .jsonPath("$.instance").isEqualTo("/products/99")
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.title").isEqualTo("Not Found")
                .jsonPath("$.type").doesNotExist();
    }

    @Test
    void coffeeIsStillThere() {
        this.client.get().uri("/products/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Coffee")
                .jsonPath("$.price").isEqualTo(4.50);
    }

    @Test
    void invalidPostNamesTheFields() {
        this.client.post().uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"name\":\"\",\"price\":-1}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Invalid request content.")
                .jsonPath("$.instance").isEqualTo("/products")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.title").isEqualTo("Bad Request")
                .jsonPath("$.type").doesNotExist()
                .jsonPath("$.properties").doesNotExist()
                .jsonPath("$.errors.name").isEqualTo("must not be blank")
                .jsonPath("$.errors.price").isEqualTo("must be greater than 0");
    }

    @Test
    void addsCocoa() {
        this.client.post().uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"name\":\"Cocoa\",\"price\":3.90}")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("/products/3")
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.name").isEqualTo("Cocoa")
                .jsonPath("$.price").isEqualTo(3.90);
    }

}
