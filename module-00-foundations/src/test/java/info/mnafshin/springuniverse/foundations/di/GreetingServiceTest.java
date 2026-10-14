package info.mnafshin.springuniverse.foundations.di;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

class GreetingServiceTest {

    @Test
    void usesTheInjectedDependency() {
        assertThat(new GreetingService(new EnglishMessages()).greet(" Ada ")).isEqualTo("Hello, Ada!");
    }

    @Test
    void injectionMakesSwappingEasy() {
        MessageSource fake = name -> "Hi " + name; // a test double, no framework needed
        assertThat(new GreetingService(fake).greet("Ada")).isEqualTo("Hi Ada");
    }

    @Test
    void requiresADependency() {
        assertThatNullPointerException().isThrownBy(() -> new GreetingService(null));
    }
}
