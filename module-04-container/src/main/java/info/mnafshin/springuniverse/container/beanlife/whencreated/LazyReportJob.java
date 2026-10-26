package info.mnafshin.springuniverse.container.beanlife.whencreated;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/** The same job with @Lazy: created the first time someone asks for it. */
@Component
@Lazy
public class LazyReportJob {

    public LazyReportJob() {
        System.out.println("LazyReportJob created");
    }
}
