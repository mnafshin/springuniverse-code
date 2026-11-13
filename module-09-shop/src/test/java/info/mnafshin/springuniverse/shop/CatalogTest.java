package info.mnafshin.springuniverse.shop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository repository;

    @Test
    void seedsCoffeeAndTeaThroughTheRepository() {
        assertThat(this.repository.count()).isEqualTo(2);
        assertThat(this.repository.findById(1L)).get().extracting(Product::getName).isEqualTo("Coffee");
    }

    @Test
    void listsCoffeeAndTeaFromTheDatabase() throws Exception {
        this.mvc.perform(get("/catalog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Coffee"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Tea"));
    }

    @Test
    void readsCoffeeById() throws Exception {
        this.mvc.perform(get("/catalog/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Coffee"));
    }

    @Test
    void missingProductIsNotFound() throws Exception {
        this.mvc.perform(get("/catalog/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findsTeaByName() throws Exception {
        this.mvc.perform(get("/catalog").param("name", "Tea"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Tea"));
    }

    @Test
    void derivedQueryReadsTheNameColumn() {
        assertThat(this.repository.findByName("Coffee")).extracting(Product::getId).containsExactly(1L);
        assertThat(this.repository.findByName("Juice")).isEmpty();
    }
}
