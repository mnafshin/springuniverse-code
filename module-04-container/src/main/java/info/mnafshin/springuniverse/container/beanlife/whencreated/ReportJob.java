package info.mnafshin.springuniverse.container.beanlife.whencreated;

import org.springframework.stereotype.Component;

/** Rarely used and slow to build, but as a normal singleton it is still built at every startup. */
@Component
public class ReportJob {

    public ReportJob() {
        System.out.println("ReportJob created");
    }
}
