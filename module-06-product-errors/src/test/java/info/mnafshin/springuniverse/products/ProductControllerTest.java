package info.mnafshin.springuniverse.products;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void listsTheTwoProducts() throws Exception {
        this.mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Coffee"))
                .andExpect(jsonPath("$[0].price").value(4.50))
                .andExpect(jsonPath("$[1].name").value("Tea"))
                .andExpect(jsonPath("$[1].price").value(3.20));
    }

    @Test
    void readsCoffee() throws Exception {
        this.mvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Coffee"));
    }

    @Test
    void missingProductIsNotFound() throws Exception {
        this.mvc.perform(get("/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void addsCocoa() throws Exception {
        this.mvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Cocoa\",\"price\":3.90}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/products/3"))
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void changesThePrice() throws Exception {
        this.mvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Coffee\",\"price\":4.10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(4.10));
    }

    @Test
    void changeMissingIsNotFound() throws Exception {
        this.mvc.perform(put("/products/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Coffee\",\"price\":4.10}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void removesTea() throws Exception {
        this.mvc.perform(delete("/products/2"))
                .andExpect(status().isNoContent());
        this.mvc.perform(get("/products/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void removeMissingIsNotFound() throws Exception {
        this.mvc.perform(delete("/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void rejectsABlankNameAndANegativePrice() throws Exception {
        this.mvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"price\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(jsonPath("$.instance").value("/products"))
                .andExpect(jsonPath("$.type").doesNotExist());
        this.mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void rejectsABadChange() throws Exception {
        this.mvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"price\":-1}"))
                .andExpect(status().isBadRequest());
    }

}
