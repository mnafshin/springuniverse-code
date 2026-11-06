package info.mnafshin.springuniverse.products;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

/**
 * Hits the embedded server. MockMvc records the 400 status from the dispatcher
 * but does not dispatch to Boot's /error controller, so the classic JSON body
 * is only visible over HTTP.
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
    void invalidPostIsClassicBootJson() {
        this.client.post().uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"name\":\"\",\"price\":-1}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.error").isEqualTo("Bad Request")
                .jsonPath("$.path").isEqualTo("/products")
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.message").doesNotExist()
                .jsonPath("$.title").doesNotExist()
                .jsonPath("$.detail").doesNotExist()
                .jsonPath("$.type").doesNotExist()
                .jsonPath("$.instance").doesNotExist();
    }

}
