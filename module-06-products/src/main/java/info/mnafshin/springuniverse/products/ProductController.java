package info.mnafshin.springuniverse.products;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ProductController {

    private final Map<Long, Product> products = new ConcurrentSkipListMap<>();
    private final AtomicLong nextId = new AtomicLong(3);

    ProductController() {
        products.put(1L, new Product(1L, "Coffee", new BigDecimal("4.50")));
        products.put(2L, new Product(2L, "Tea", new BigDecimal("3.20")));
    }

    @GetMapping("/products")
    List<Product> all() {
        return List.copyOf(products.values());
    }

    @GetMapping("/products/{id}")
    ResponseEntity<Product> one(@PathVariable Long id) {
        Product p = products.get(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @PostMapping("/products")
    ResponseEntity<Product> add(@RequestBody ProductRequest request) {
        long id = nextId.getAndIncrement();
        Product p = new Product(id, request.name(), request.price());
        products.put(id, p);
        return ResponseEntity.created(URI.create("/products/" + id)).body(p);
    }

    @PutMapping("/products/{id}")
    ResponseEntity<Product> change(@PathVariable Long id, @RequestBody ProductRequest request) {
        if (!products.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        Product p = new Product(id, request.name(), request.price());
        products.put(id, p);
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/products/{id}")
    ResponseEntity<Void> remove(@PathVariable Long id) {
        return products.remove(id) != null ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
