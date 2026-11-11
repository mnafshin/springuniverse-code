package info.mnafshin.springuniverse.shop;

import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.GetMapping;
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

}
