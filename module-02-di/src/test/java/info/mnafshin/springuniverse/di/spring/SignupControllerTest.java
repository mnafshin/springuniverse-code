package info.mnafshin.springuniverse.di.spring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.client.RestTestClient;

/** The whole app on a random port: controller -> service -> repository + notifier, all wired by Spring. */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class SignupControllerTest {

    @Autowired
    RestTestClient client;

    @Autowired
    UserRepository users;

    @Test
    void postSignupReturnsTheWelcome() {
        client.post().uri("/signups?name=Ada")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Welcome.class).isEqualTo(new Welcome("Ada", "Welcome, Ada!"));

        assertThat(users.findAll()).contains("Ada");
    }
}
