package info.mnafshin.springuniverse.hello;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.client.RestTestClient;

import info.mnafshin.springuniverse.hello.ThreadController.ThreadInfo;

/** application.properties sets spring.threads.virtual.enabled=true, so real Tomcat requests run on virtual threads. */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class VirtualThreadsTest {

    @Autowired
    RestTestClient client;

    @Test
    void requestRunsOnAVirtualThread() {
        ThreadInfo info = client.get().uri("/thread").exchange()
                .expectStatus().isOk()
                .expectBody(ThreadInfo.class).returnResult().getResponseBody();

        assertThat(info).isNotNull();
        assertThat(info.virtual()).isTrue();
        assertThat(info.thread()).startsWith("VirtualThread");
    }
}
