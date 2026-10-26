package info.mnafshin.springuniverse.container.beanlife.whencreated;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** When is a bean created? Singletons at startup, once. @Lazy beans on first use. */
public class WhenCreatedApp {

    public static void main(String[] args) {
        System.out.println("-- normal singletons");
        System.out.println("starting the container");
        try (var context = new AnnotationConfigApplicationContext(Menu.class, ReportJob.class)) {
            System.out.println("container ready");
            ReportJob first = context.getBean(ReportJob.class);
            ReportJob second = context.getBean(ReportJob.class);
            System.out.println("asked twice, same object: " + (first == second));
        }

        System.out.println("-- with @Lazy");
        System.out.println("starting the container");
        try (var context = new AnnotationConfigApplicationContext(Menu.class, LazyReportJob.class)) {
            System.out.println("container ready");
            System.out.println("asking for the report job");
            context.getBean(LazyReportJob.class);
        }
    }
}
