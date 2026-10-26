package info.mnafshin.springuniverse.container.postproc.plan;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class ReportJob {

    public ReportJob() {
        System.out.println("ReportJob created");
    }
}
