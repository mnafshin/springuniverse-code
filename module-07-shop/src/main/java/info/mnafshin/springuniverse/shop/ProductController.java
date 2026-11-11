package info.mnafshin.springuniverse.shop;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ProductController {

    private final Map<Long, Product> products = Map.of(
            1L, new Product(1L, "Coffee", new BigDecimal("4.50")),
            2L, new Product(2L, "Tea", new BigDecimal("3.20")));

    @GetMapping("/products")
    List<Product> all() {
        return List.of(products.get(1L), products.get(2L));
    }

    @GetMapping("/products/{id}")
    ResponseEntity<Product> one(@PathVariable Long id) {
        Product p = products.get(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

}
