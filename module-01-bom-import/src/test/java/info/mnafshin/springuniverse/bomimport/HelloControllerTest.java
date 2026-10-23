package info.mnafshin.springuniverse.bomimport;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    MockMvcTester mvc;

    @Test
    void greetsByNameThanksToParameterNames() {
        assertThat(mvc.get().uri("/hello").param("name", "Ada"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.message").isEqualTo("Hello, Ada!");
    }

    @Test
    void greetsTheWorldByDefault() {
        assertThat(mvc.get().uri("/hello"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.message").isEqualTo("Hello, World!");
    }
}
