package info.mnafshin.springuniverse.hello;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

/** Starts the real app with embedded Tomcat on a random port and calls it over HTTP. */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class HelloEndToEndTest {

    @Autowired
    RestTestClient client;

    @Test
    void returnsJsonGreeting() {
        client.get().uri("/hello?name=Ada")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody(Greeting.class).isEqualTo(new Greeting("Hello, Ada!"));
    }
}
