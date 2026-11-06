package info.mnafshin.springuniverse.products;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

/**
 * Hits the embedded server so error bodies match curl. MockMvc does not dispatch
 * to Boot's /error page, which is where the classic JSON used to be written.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class ProductErrorHttpTest {

    @Autowired
    private RestTestClient client;

    @Test
    void missingProductIsAnEmptyNotFound() {
        this.client.get().uri("/products/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }

    @Test
    void invalidPostIsProblemDetailWithoutFieldErrors() {
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
                .jsonPath("$.errors").doesNotExist();
    }

}
