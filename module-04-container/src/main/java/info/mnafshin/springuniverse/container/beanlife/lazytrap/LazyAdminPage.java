package info.mnafshin.springuniverse.container.beanlife.lazytrap;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/** The fix: @Lazy on the injection point too. Spring injects a proxy and creates the report on the first call. */
@Component
public class LazyAdminPage {

    private final SalesReport report;

    public LazyAdminPage(@Lazy SalesReport report) {
        this.report = report;
        System.out.println("LazyAdminPage created, report is a " + report.getClass().getSimpleName());
    }

    public void open() {
        System.out.println(report.totals());
    }
}
