package info.mnafshin.springuniverse.shop;

import java.math.BigDecimal;

import info.mnafshin.springuniverse.prices.PriceFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PriceController {

    private final PriceFormatter prices;

    PriceController(PriceFormatter prices) {
        this.prices = prices;
    }

    @GetMapping("/")
    String coffee() {
        return "Coffee: " + this.prices.format(new BigDecimal("4.5"));
    }

}
