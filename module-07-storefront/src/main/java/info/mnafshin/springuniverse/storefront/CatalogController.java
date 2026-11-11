package info.mnafshin.springuniverse.storefront;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@RestController
class CatalogController {

    private final RestClient restClient;
    private final ProductClient productClient;

    CatalogController(RestClient.Builder builder, ProductClient productClient) {
        this.restClient = builder.baseUrl("http://localhost:8080").build();
        this.productClient = productClient;
    }

    @GetMapping("/catalog")
    List<Product> catalog() {
        return restClient.get()
                .uri("/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {});
    }

    @GetMapping("/catalog/{id}")
    ResponseEntity<Product> one(@PathVariable Long id) {
        try {
            ResponseEntity<Product> shop = productClient.one(id);
            return ResponseEntity.status(shop.getStatusCode()).body(shop.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

}
