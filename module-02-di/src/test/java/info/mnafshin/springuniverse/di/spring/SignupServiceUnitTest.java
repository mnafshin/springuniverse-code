package info.mnafshin.springuniverse.di.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/** The @Service is still a plain class: test it with new, no container needed. */
class SignupServiceUnitTest {

    @Test
    void worksWithoutSpring() {
        List<String> sent = new ArrayList<>();
        var signup = new SignupService(new InMemoryUserRepository(), (to, message) -> sent.add(to + ": " + message));

        assertThat(signup.register("Ada")).isEqualTo("Welcome, Ada!");
        assertThat(sent).containsExactly("Ada: Welcome, Ada!");
    }
}
