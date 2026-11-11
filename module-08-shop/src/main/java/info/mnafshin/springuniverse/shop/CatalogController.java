package info.mnafshin.springuniverse.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class CatalogController {

    private final JdbcClient jdbcClient;

    CatalogController(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @GetMapping("/catalog")
    List<Product> all() {
        return jdbcClient.sql("SELECT id, name FROM products ORDER BY id")
                .query(Product.class)
                .list();
    }

    @GetMapping("/catalog/{id}")
    ResponseEntity<Product> one(@PathVariable Long id) {
        Optional<Product> product = jdbcClient.sql("SELECT id, name FROM products WHERE id = :id")
                .param("id", id)
                .query(Product.class)
                .optional();
        return ResponseEntity.of(product);
    }

}
