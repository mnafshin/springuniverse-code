package info.mnafshin.springuniverse.container.shop.whencreated;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** When is a bean created? Singletons at startup, once. @Lazy beans on first use. */
public class WhenCreatedApp {

    public static void main(String[] args) {
        System.out.println("-- normal singletons");
        System.out.println("starting the container");
        try (var context = new AnnotationConfigApplicationContext(PriceCache.class, SalesReport.class)) {
            System.out.println("container ready");
            SalesReport first = context.getBean(SalesReport.class);
            SalesReport second = context.getBean(SalesReport.class);
            System.out.println("asked twice, same object: " + (first == second));
        }

        System.out.println("-- with @Lazy");
        System.out.println("starting the container");
        try (var context = new AnnotationConfigApplicationContext(PriceCache.class, LazySalesReport.class)) {
            System.out.println("container ready");
            System.out.println("asking for the sales report");
            context.getBean(LazySalesReport.class);
        }
    }
}
