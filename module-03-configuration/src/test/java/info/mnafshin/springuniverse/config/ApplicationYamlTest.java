package info.mnafshin.springuniverse.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** The values from src/main/resources/application.yml, bound into the record and injected like any bean. */
@SpringBootTest
class ApplicationYamlTest {

    @Autowired
    SignupProperties props;

    @Autowired
    SignupService signup;

    @Autowired
    ValueSignupService valueSignup;

    @Test
    void applicationYmlIsBoundIntoTheRecord() {
        assertThat(props).isEqualTo(new SignupProperties("Welcome", 100, Duration.ofMinutes(30)));
    }

    @Test
    void theRecordIsInjectedLikeAnyOtherBean() {
        assertThat(signup.welcome("Ada")).isEqualTo("Welcome, Ada!");
        assertThat(signup.dailyLimit()).isEqualTo(100);
    }

    @Test
    void valueReadsTheSameKeysOneByOne() {
        assertThat(valueSignup.welcomeMessage()).isEqualTo("Welcome");
        assertThat(valueSignup.dailyLimit()).isEqualTo(100);
    }
}
