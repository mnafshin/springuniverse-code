package info.mnafshin.springuniverse.shop;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class CatalogController {

    private final ProductRepository repository;

    CatalogController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/catalog")
    PagedModel<CatalogItem> catalog(Pageable pageable) {
        return new PagedModel<>(repository.findCatalog(pageable));
    }
}
