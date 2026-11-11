package info.mnafshin.springuniverse.storefront;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

interface ProductClient {

    @GetExchange("/products")
    List<Product> all();

    @GetExchange("/products/{id}")
    ResponseEntity<Product> one(@PathVariable Long id);

}
