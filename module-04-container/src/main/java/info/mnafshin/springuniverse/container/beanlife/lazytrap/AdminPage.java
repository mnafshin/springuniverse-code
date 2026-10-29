package info.mnafshin.springuniverse.container.beanlife.lazytrap;

import org.springframework.stereotype.Component;

/** A normal (eager) bean that needs the report: creating it forces the "lazy" report to be created too. */
@Component
public class AdminPage {

    private final SalesReport report;

    public AdminPage(SalesReport report) {
        this.report = report;
        System.out.println("AdminPage created");
    }

    public void open() {
        System.out.println(report.totals());
    }
}
