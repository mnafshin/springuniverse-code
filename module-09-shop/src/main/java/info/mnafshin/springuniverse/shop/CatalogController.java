package info.mnafshin.springuniverse.shop;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class CatalogController {

    private final ProductRepository repository;

    CatalogController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/catalog")
    List<Product> all() {
        return repository.findAll();
    }

    @GetMapping("/catalog/{id}")
    ResponseEntity<Product> one(@PathVariable Long id) {
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping(path = "/catalog", params = "name")
    List<Product> byName(@RequestParam String name) {
        return repository.findByName(name);
    }

}
