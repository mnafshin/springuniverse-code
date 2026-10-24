package info.mnafshin.springuniverse.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

/** Later property sources override earlier ones: test properties and command-line arguments beat application.yml. */
@SpringBootTest(properties = "app.signup.daily-limit=7")
class PrecedenceTest {

    @Autowired
    SignupProperties props;

    @Test
    void testPropertiesOverrideApplicationYml() {
        assertThat(props.dailyLimit()).isEqualTo(7);
        assertThat(props.welcomeMessage()).isEqualTo("Welcome");   // keys we did not override still come from the file
    }

    @Test
    void commandLineArgumentsOverrideApplicationYml() {
        try (ConfigurableApplicationContext ctx = SpringApplication.run(ConfigApplication.class,
                "--server.port=0", "--app.signup.daily-limit=10")) {
            assertThat(ctx.getBean(SignupProperties.class).dailyLimit()).isEqualTo(10);
        }
    }
}
