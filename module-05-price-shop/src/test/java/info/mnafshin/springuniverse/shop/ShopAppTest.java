package info.mnafshin.springuniverse.shop;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import info.mnafshin.springuniverse.prices.PriceFormatter;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * T5.3 v2: the shop's own euroPrices bean wins; the library's priceFormatter is not created (@ConditionalOnMissingBean).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShopAppTest {

    @Autowired
    ApplicationContext context;

    @Test
    void theShopsOwnFormatterWins() {
        assertThat(context.getBeanNamesForType(PriceFormatter.class)).containsExactly("euroPrices");
        assertThat(context.getBean(PriceFormatter.class).format(new BigDecimal("4.5"))).isEqualTo("4.50 EUR");
    }

}
