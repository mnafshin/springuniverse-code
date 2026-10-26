package info.mnafshin.springuniverse.container.postproc.lazyplan;

/** Pretend this class comes from a library jar: we can't add @Lazy to it. */
public class ReportJob {

    public ReportJob() {
        System.out.println("ReportJob created (slow)");
    }
}
