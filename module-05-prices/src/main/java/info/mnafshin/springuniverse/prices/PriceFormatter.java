package info.mnafshin.springuniverse.prices;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceFormatter {

    private final String currency;

    public PriceFormatter(String currency) {
        this.currency = currency;
    }

    public String format(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP) + " " + this.currency;
    }

}
