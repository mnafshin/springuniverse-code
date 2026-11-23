package info.mnafshin.springuniverse.shop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "spring.jpa.properties.hibernate.generate_statistics=true")
@AutoConfigureMockMvc
class CatalogTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository products;

    @Autowired
    private CategoryRepository categories;

    @Autowired
    private EntityManagerFactory emf;

    private Statistics statistics;

    @BeforeEach
    void clearStatistics() {
        this.statistics = this.emf.unwrap(SessionFactory.class).getStatistics();
        this.statistics.clear();
    }

    @Test
    void seedsThreeCategoriesAndFourProducts() {
        assertThat(this.categories.count()).isEqualTo(3);
        assertThat(this.products.count()).isEqualTo(4);
    }

    @Test
    void listsEveryProductWithItsCategory() throws Exception {
        this.mvc.perform(get("/catalog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(4))
                .andExpect(jsonPath("$.content[0].name").value("Coffee"))
                .andExpect(jsonPath("$.content[0].category").value("Drinks"))
                .andExpect(jsonPath("$.content[2].name").value("Croissant"))
                .andExpect(jsonPath("$.content[2].category").value("Bakery"))
                .andExpect(jsonPath("$.content[3].name").value("Espresso beans"))
                .andExpect(jsonPath("$.content[3].category").value("Beans"));
    }

    @Test
    void secondPageOfTwoHoldsCroissantAndEspressoBeans() throws Exception {
        this.mvc.perform(get("/catalog").param("page", "1").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Croissant"))
                .andExpect(jsonPath("$.content[1].name").value("Espresso beans"))
                .andExpect(jsonPath("$.page.totalElements").value(4))
                .andExpect(jsonPath("$.page.totalPages").value(2));
    }

    @Test
    void onePageIsOneQueryForTheRowsAndOneToCountThem() {
        Page<CatalogItem> page = this.products.findCatalog(PageRequest.of(0, 2));
        assertThat(page.getContent()).containsExactly(
                new CatalogItem("Coffee", "Drinks"), new CatalogItem("Tea", "Drinks"));
        assertThat(page.getTotalElements()).isEqualTo(4);
        assertThat(this.statistics.getPrepareStatementCount()).isEqualTo(2);
    }

    @Test
    void theProjectionGivesNameAndCategoryInOneQuery() {
        assertThat(this.products.findCatalog(Pageable.unpaged())).containsExactly(
                new CatalogItem("Coffee", "Drinks"),
                new CatalogItem("Tea", "Drinks"),
                new CatalogItem("Croissant", "Bakery"),
                new CatalogItem("Espresso beans", "Beans"));
        assertThat(this.statistics.getPrepareStatementCount()).isEqualTo(1);
        assertThat(this.statistics.getEntityLoadCount()).isZero();
    }

    @Test
    void findAllStillLoadsProductsWithTheirCategoryInOneQuery() {
        assertThat(this.products.findAll()).extracting(p -> p.getCategory().getName())
                .containsExactly("Drinks", "Drinks", "Bakery", "Beans");
        assertThat(this.statistics.getPrepareStatementCount()).isEqualTo(1);
    }
}
