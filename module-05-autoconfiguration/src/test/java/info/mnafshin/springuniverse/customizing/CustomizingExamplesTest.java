package info.mnafshin.springuniverse.customizing;

import info.mnafshin.springuniverse.customizing.compact.CompactJsonApp;
import info.mnafshin.springuniverse.customizing.exclude.right.ExcludeApp;
import info.mnafshin.springuniverse.customizing.exclude.wrong.WrongExcludeApp;
import info.mnafshin.springuniverse.customizing.pretty.PrettyJsonApp;
import info.mnafshin.springuniverse.customizing.tweak.TweakJsonApp;
import info.mnafshin.springuniverse.notifier.Notifier;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/** The T5.2 examples in package {@code customizing}, as tests. */
class CustomizingExamplesTest {

    private static final String SNAKE = "--spring.jackson.property-naming-strategy=SNAKE_CASE";

    private static ConfigurableApplicationContext start(Class<?> app, String... args) {
        return new SpringApplicationBuilder(app).web(WebApplicationType.NONE).run(args);
    }

    private static String json(Class<?> app, String... args) {
        try (ConfigurableApplicationContext context = start(app, args)) {
            return context.getBean(JsonMapper.class).writeValueAsString(Order.SAMPLE);
        }
    }

    @Test
    void bootsDefaultMapperWritesOneLine() {
        assertThat(json(CompactJsonApp.class)).isEqualTo("{\"customerName\":\"Ada\",\"itemCount\":3}");
    }

    @Test
    void ourOwnMapperReplacesBootsAndIndents() {
        try (ConfigurableApplicationContext context = start(PrettyJsonApp.class)) {
            assertThat(context.getBeanNamesForType(JsonMapper.class)).containsExactly("jsonMapper");
            assertThat(context.getBean(JsonMapper.class).writeValueAsString(Order.SAMPLE)).contains("\n");
        }
    }

    @Test
    void ourOwnMapperIgnoresSpringJacksonProperties() {
        assertThat(json(PrettyJsonApp.class, SNAKE)).contains("\"customerName\"").doesNotContain("customer_name");
    }

    @Test
    void aCustomizerKeepsBootsMapperAndItsProperties() {
        String json = json(TweakJsonApp.class, SNAKE);
        assertThat(json).contains("\"customer_name\"").contains("\n");
    }

    @Test
    void excludingANonAutoConfigurationClassFails() {
        assertThatIllegalStateException().isThrownBy(() -> start(WrongExcludeApp.class).close())
            .withMessageContaining("could not be excluded because they are not auto-configuration classes");
    }

    @Test
    void excludingTheAutoConfigurationRemovesTheNotifier() {
        try (ConfigurableApplicationContext context = start(ExcludeApp.class)) {
            assertThat(context.getBeanNamesForType(Notifier.class)).isEmpty();
        }
    }

}
