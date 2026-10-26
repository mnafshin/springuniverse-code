package info.mnafshin.springuniverse.container.checkout.whatis;

import org.springframework.stereotype.Service;

/** No annotations on its methods: Spring hands out the object itself. */
@Service
public class ProductCatalog {

    public String items() {
        return "coffee, cake";
    }
}
