package info.mnafshin.springuniverse.container.lifecycle;

/** Made lazy by MakeReportJobLazy: not created at startup, only on first use. */
public class ReportJob {

    public ReportJob() {
        Events.log("ReportJob created");
    }
}
