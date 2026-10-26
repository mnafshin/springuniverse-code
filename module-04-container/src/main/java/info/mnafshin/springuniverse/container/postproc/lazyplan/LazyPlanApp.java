package info.mnafshin.springuniverse.container.postproc.lazyplan;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** A library bean slows down startup. A BeanFactoryPostProcessor makes it lazy without touching its class. */
public class LazyPlanApp {

    public static void main(String[] args) {
        System.out.println("-- without a post-processor");
        try (var context = new AnnotationConfigApplicationContext(ShopConfig.class)) {
            System.out.println("container ready");
        }
        System.out.println("-- with MakeReportJobLazy");
        try (var context = new AnnotationConfigApplicationContext(LazyShopConfig.class)) {
            System.out.println("container ready");
            System.out.println("asking for the report job");
            context.getBean(ReportJob.class);
        }
    }
}
