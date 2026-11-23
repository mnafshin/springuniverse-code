package info.mnafshin.springuniverse.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @EntityGraph(attributePaths = "category")
    List<Product> findAll();

    @Query("select p.name, c.name from Product p join p.category c order by p.id")
    Page<CatalogItem> findCatalog(Pageable pageable);
}
