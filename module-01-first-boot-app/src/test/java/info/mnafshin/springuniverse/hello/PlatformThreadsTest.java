package info.mnafshin.springuniverse.hello;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.client.RestTestClient;

import info.mnafshin.springuniverse.hello.ThreadController.ThreadInfo;

/** Same app with the property switched off: Tomcat's classic platform worker threads (http-nio-...-exec-N). */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "spring.threads.virtual.enabled=false")
@AutoConfigureRestTestClient
class PlatformThreadsTest {

    @Autowired
    RestTestClient client;

    @Test
    void requestRunsOnAPlatformThread() {
        ThreadInfo info = client.get().uri("/thread").exchange()
                .expectStatus().isOk()
                .expectBody(ThreadInfo.class).returnResult().getResponseBody();

        assertThat(info).isNotNull();
        assertThat(info.virtual()).isFalse();
        assertThat(info.thread()).contains("http-nio-").contains("-exec-");
    }
}
