package info.mnafshin.springuniverse.storefront;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

interface ProductClient {

    @GetExchange("/products/{id}")
    Product one(@PathVariable Long id);

}
