package info.mnafshin.springuniverse.config.app;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/** Still plain Java: a fixed Clock and a recording notifier, no container. */
class SignupServiceTest {

    @Test
    void usesTheInjectedClock() {
        List<String> sent = new ArrayList<>();
        Clock fixed = Clock.fixed(Instant.parse("2026-10-24T12:00:00Z"), ZoneOffset.UTC);
        var signup = new SignupService(new InMemoryUserRepository(new AuditLog()), (to, msg) -> sent.add(to + ": " + msg), fixed);

        signup.register("Ada");

        assertThat(sent).containsExactly("Ada: Welcome, Ada! (signed up 2026-10-24T12:00:00Z)");
    }
}
