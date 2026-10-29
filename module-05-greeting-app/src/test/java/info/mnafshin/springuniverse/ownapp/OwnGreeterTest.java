package info.mnafshin.springuniverse.ownapp;

import info.mnafshin.springuniverse.greeting.Greeter;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/** With the app's own greeter, the library's default backs off: one greeter, the app's. */
@SpringBootTest
class OwnGreeterTest {

    @Autowired
    ApplicationContext context;

    @Test
    void appGreeterWins() {
        assertThat(context.getBeanNamesForType(Greeter.class)).containsExactly("myGreeter");
        assertThat(context.getBean(Greeter.class).greet("Ada")).isEqualTo("Hi Ada");
    }

}
