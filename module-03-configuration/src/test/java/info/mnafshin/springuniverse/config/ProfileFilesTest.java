package info.mnafshin.springuniverse.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** application-dev.yml is merged on top of application.yml, key by key. */
@SpringBootTest
@ActiveProfiles("dev")
class ProfileFilesTest {

    @Autowired
    SignupProperties props;

    @Autowired
    SignupLimits limits;

    @Autowired
    WelcomeNotifier notifier;

    @Test
    void devFileOverridesOnlyTheKeysItContains() {
        assertThat(props.welcomeMessage()).isEqualTo("Welcome, developer");
        assertThat(props.dailyLimit()).isEqualTo(5);
        assertThat(props.linkExpiry()).isEqualTo(Duration.ofMinutes(30));   // not in the dev file: still from application.yml
    }

    @Test
    void spelWarnAtFollowsTheProfileValue() {
        assertThat(limits.warnAt()).isEqualTo(4);
    }

    @Test
    void devGetsTheLoggingNotifier() {
        assertThat(notifier).isInstanceOf(LogWelcomeNotifier.class);
    }
}
