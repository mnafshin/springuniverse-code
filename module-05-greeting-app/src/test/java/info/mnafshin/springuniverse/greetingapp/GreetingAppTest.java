package info.mnafshin.springuniverse.greetingapp;

import info.mnafshin.springuniverse.greeting.DefaultGreeter;
import info.mnafshin.springuniverse.greeting.Greeter;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/** The app gets the starter's default greeter without defining or scanning it. */
@SpringBootTest
class GreetingAppTest {

    @Autowired
    Greeter greeter;

    @Test
    void greeterComesFromTheStarter() {
        assertThat(greeter).isInstanceOf(DefaultGreeter.class);
        assertThat(greeter.greet("Ada")).isEqualTo("Hello, Ada!");
    }

}
