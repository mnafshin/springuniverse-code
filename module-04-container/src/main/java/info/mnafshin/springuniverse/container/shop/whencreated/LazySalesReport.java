package info.mnafshin.springuniverse.container.shop.whencreated;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/** The same report with @Lazy: created the first time someone asks for it. */
@Component
@Lazy
public class LazySalesReport {

    public LazySalesReport() {
        System.out.println("LazySalesReport created");
    }
}
