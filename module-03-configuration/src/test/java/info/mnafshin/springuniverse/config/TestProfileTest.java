package info.mnafshin.springuniverse.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

/** @ActiveProfiles("test") picks up src/test/resources/application-test.yml. */
@SpringBootTest
@ActiveProfiles("test")
class TestProfileTest {

    @Autowired
    SignupProperties props;

    @Autowired
    Environment environment;

    @Test
    void testProfileFileIsUsed() {
        assertThat(environment.getActiveProfiles()).containsExactly("test");
        assertThat(props.dailyLimit()).isEqualTo(3);
        assertThat(props.welcomeMessage()).isEqualTo("Welcome");
    }
}
