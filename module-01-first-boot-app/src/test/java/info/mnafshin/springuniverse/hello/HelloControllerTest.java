package info.mnafshin.springuniverse.hello;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    MockMvcTester mvc;

    @Test
    void greetsTheWorldByDefault() {
        assertThat(mvc.get().uri("/hello"))
                .hasStatusOk()
                .hasContentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .bodyJson().isLenientlyEqualTo("""
                        {"message":"Hello, World!"}
                        """);
    }

    @Test
    void greetsByName() {
        assertThat(mvc.get().uri("/hello").param("name", "Ada"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.message").isEqualTo("Hello, Ada!");
    }

    @Test
    void onlyGetIsMapped() {
        assertThat(mvc.post().uri("/hello")).hasStatus(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Test
    void unknownPathIsNotFound() {
        assertThat(mvc.get().uri("/nope")).hasStatus(HttpStatus.NOT_FOUND);
    }
}
