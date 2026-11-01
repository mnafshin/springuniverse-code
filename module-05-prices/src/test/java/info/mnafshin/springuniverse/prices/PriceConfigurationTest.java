package info.mnafshin.springuniverse.prices;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * T5.3 v2 (not shown in the video): without an app bean the library gives USD; with one, it steps aside.
 */
class PriceConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(PriceConfiguration.class));

    @Test
    void givesItsOwnFormatterWhenTheAppHasNone() {
        this.runner.run((context) -> assertThat(context.getBean(PriceFormatter.class).format(new BigDecimal("4.5")))
            .isEqualTo("4.50 USD"));
    }

    @Test
    void stepsAsideWhenTheAppHasItsOwn() {
        this.runner.withBean("euroPrices", PriceFormatter.class, () -> new PriceFormatter("EUR"))
            .run((context) -> assertThat(context.getBeansOfType(PriceFormatter.class)).containsOnlyKeys("euroPrices"));
    }

}
