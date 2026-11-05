package info.mnafshin.springuniverse.products;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
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

}
