package info.mnafshin.springuniverse.container.shop.whencreated;

import org.springframework.stereotype.Component;

/** Rarely used and slow to build, but as a normal singleton it is still built at every startup. */
@Component
public class SalesReport {

    public SalesReport() {
        System.out.println("SalesReport created");
    }
}
