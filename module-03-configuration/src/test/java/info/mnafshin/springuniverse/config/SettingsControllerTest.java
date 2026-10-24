package info.mnafshin.springuniverse.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.client.RestTestClient;

/** The whole app on a random port: GET /settings shows the bound values. */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class SettingsControllerTest {

    @Autowired
    RestTestClient client;

    @Test
    void settingsShowsTheBoundValues() {
        client.get().uri("/settings")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.welcomeMessage").isEqualTo("Welcome")
                .jsonPath("$.dailyLimit").isEqualTo(100);
    }
}
