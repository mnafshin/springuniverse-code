package info.mnafshin.springuniverse.autoconfig;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

/** The whole app with no profile: the default {@code LogNotifier} and Boot's own compact JSON. */
@SpringBootTest
@AutoConfigureMockMvc
class SignupControllerTest {

    @Autowired
    MockMvcTester mvc;

    @Test
    void usesTheAutoConfiguredDefaultNotifier() {
        assertThat(mvc.get().uri("/signup?name=Ada"))
            .hasStatusOk()
            .bodyText()
            .isEqualTo("{\"name\":\"Ada\",\"notifier\":\"log\",\"result\":\"logged for Ada\"}");
    }

}
