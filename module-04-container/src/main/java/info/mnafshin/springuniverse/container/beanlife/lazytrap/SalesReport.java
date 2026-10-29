package info.mnafshin.springuniverse.container.beanlife.lazytrap;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/** Marked @Lazy: Spring itself won't create it at startup. */
@Component
@Lazy
public class SalesReport {

    public SalesReport() {
        System.out.println("SalesReport created");
    }

    public String totals() {
        return "sales: 42";
    }
}
