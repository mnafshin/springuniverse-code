package info.mnafshin.springuniverse.tuning;

import info.mnafshin.springuniverse.customizing.Order;
import info.mnafshin.springuniverse.notifier.Notifier;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/** The T5.2 shop apps (package tuning), started the way their {@code main} methods start them. */
class ShopTuningTest {

    static final String SNAKE = "--spring.jackson.property-naming-strategy=SNAKE_CASE";

    static final String INDENT = "--spring.jackson.serialization.indent-output=true";

    static ConfigurableApplicationContext run(Class<?> app, String... args) {
        return new SpringApplicationBuilder(app).web(WebApplicationType.NONE).run(args);
    }

    static String json(ConfigurableApplicationContext context) {
        return context.getBean(JsonMapper.class).writeValueAsString(Order.SAMPLE);
    }

    @Test
    void theDefaultMapperFollowsTheProperty() {
        try (ConfigurableApplicationContext context = run(info.mnafshin.springuniverse.tuning.plain.ShopApp.class)) {
            assertThat(json(context)).isEqualTo("{\"customerName\":\"Ada\",\"itemCount\":3}");
            assertThat(context.getBean(Notifier.class).name()).isEqualTo("log");
        }
        try (ConfigurableApplicationContext context = run(info.mnafshin.springuniverse.tuning.plain.ShopApp.class, SNAKE)) {
            assertThat(json(context)).isEqualTo("{\"customer_name\":\"Ada\",\"item_count\":3}");
        }
    }

    @Test
    void replacingTheMapperIgnoresTheProperty() {
        try (ConfigurableApplicationContext context = run(info.mnafshin.springuniverse.tuning.replace.ShopApp.class, SNAKE)) {
            assertThat(json(context)).contains("\n").contains("customerName").doesNotContain("customer_name");
        }
    }

    @Test
    void aCustomizerKeepsTheProperty() {
        try (ConfigurableApplicationContext context = run(info.mnafshin.springuniverse.tuning.customize.ShopApp.class, SNAKE)) {
            assertThat(json(context)).contains("\n").contains("customer_name").doesNotContain("customerName");
        }
    }

    @Test
    void twoPropertiesNeedNoCode() {
        try (ConfigurableApplicationContext context = run(info.mnafshin.springuniverse.tuning.plain.ShopApp.class, SNAKE, INDENT)) {
            assertThat(json(context)).contains("\n").contains("customer_name");
        }
    }

    @Test
    void excludingABeanClassFails() {
        assertThatIllegalStateException().isThrownBy(() -> run(info.mnafshin.springuniverse.tuning.offwrong.ShopApp.class).close())
            .withMessageContaining("could not be excluded because they are not auto-configuration classes");
    }

    @Test
    void excludingTheAutoConfigurationRemovesTheNotifier() {
        try (ConfigurableApplicationContext context = run(info.mnafshin.springuniverse.tuning.off.ShopApp.class)) {
            assertThat(context.getBeanNamesForType(Notifier.class)).isEmpty();
        }
        try (ConfigurableApplicationContext context = run(info.mnafshin.springuniverse.tuning.plain.ShopApp.class,
                "--spring.autoconfigure.exclude=info.mnafshin.springuniverse.notifier.NotifierAutoConfiguration")) {
            assertThat(context.getBeanNamesForType(Notifier.class)).isEmpty();
        }
    }

}
